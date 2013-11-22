package jpcap.media;

import java.util.concurrent.ExecutorService;

public abstract class Dispatcher implements Runnable {
	protected PacketQueue q;
	protected ExecutorService exec;
	protected ResultProcesser processer;

	protected boolean mediaGet = false;
	protected boolean mediaRes = false;

	protected Dispatcher(PacketQueue queue, ExecutorService executor,
			ResultProcesser processer) {
		this.q = queue;
		this.exec = executor;
		this.processer = processer;
	}

	@Override
	public abstract void run() ;
}
