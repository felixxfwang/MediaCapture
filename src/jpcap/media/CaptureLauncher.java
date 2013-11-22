package jpcap.media;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.htmlparser.util.ParserException;

import jpcap.*;
import jpcap.media.audio.Audio;
import jpcap.media.audio.AudioDispatcher;
import jpcap.media.video.PlayListDetector;
import jpcap.media.video.Video;
import jpcap.media.video.VideoDispatcher;

public class CaptureLauncher {
	private static final int MAX_CAPTURE_LENGTH = 3072;
	private static final int MAX_TO_PROCESS = 10000;
	private static Logger logger = LogManager.getLogger(PacketAnalyst.class);
	private static ExecutorService executor;

	public static boolean oneTimeCaptureEnd = false;

	/**
	 * 进行视频包的捕获分析
	 * 
	 * @param type
	 *            视频品质类型,"hd2"-超清,"mp4"-高清,"flv"-流畅
	 * @param processer
	 *            分析结果处理器
	 */
	public static void captureVideo(String type, ResultProcesser processer)
			throws ParseException, InterruptedException, ParserException,
			IOException {
		if (executor != null) {
			oneTimeCaptureEnd = true;
			executor.shutdownNow();
			Thread.sleep(1000);
		}
		executor = Executors.newCachedThreadPool();
		PacketQueue packetQueue = new PacketQueue();
		oneTimeCaptureEnd = false;
		startCapturer(executor, packetQueue);
		PlayListDetector detector = new PlayListDetector(packetQueue);
		Video video = detector.findPlayList();
		executor.execute(new VideoDispatcher(packetQueue, type, video,
				executor, processer));
	}

	/**
	 * 进行音频包的捕获分析
	 * 
	 * @param processer
	 *            分析结果处理器
	 * @throws ParseException
	 * @throws InterruptedException
	 */
	public static void captureAudio(ResultProcesser processer) throws InterruptedException {
		if (executor != null) {
			oneTimeCaptureEnd = true;
			executor.shutdownNow();
			Thread.sleep(1000);
		}
		executor = Executors.newCachedThreadPool();
		PacketQueue packetQueue = new PacketQueue();
		oneTimeCaptureEnd = false;
		startCapturer(executor, packetQueue);
		Audio audio = new Audio();
		executor.execute(new AudioDispatcher(packetQueue, audio, executor,
				processer));
	}

	private static void startCapturer(ExecutorService executor,
			PacketQueue packetQueue) {
		try {
			NetworkInterface[] devices = JpcapCaptor.getDeviceList();
			for (int i = 0; i < devices.length; ++i) {
				JpcapCaptor jc = JpcapCaptor.openDevice(devices[i],
						MAX_CAPTURE_LENGTH, false, MAX_TO_PROCESS);
				jc.setFilter("ip and tcp", true);
				executor.execute(new Capturer(jc, packetQueue));
			}
		} catch (Exception ef) {
			ef.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			captureVideo("mp4", new ResultProcesser() {

				@Override
				public void processVelocity(long velocity,
						long averageVelocity, long time) {
					logger.info("velocity:" + velocity + "|average velocity:"
							+ averageVelocity + "|totalDelataTime:" + time);
				}

				@Override
				public void processLostPackets(int lostPackets, long time) {
					logger.info("lostPackets:" + lostPackets + "|time:" + time);
				}

				@Override
				public void processFluency(long fluency, long time) {
					logger.info("fluency:" + fluency + "|time:" + time);
				}

				@Override
				public void segmentEnd() {
					logger.info("SegmentEnd");
				}

				@Override
				public void allEnd() {
					logger.info("AllEnd");
				}
			});
			System.out.println("input:");
			System.in.read();
			oneTimeCaptureEnd = true;
			// executor.shutdownNow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
