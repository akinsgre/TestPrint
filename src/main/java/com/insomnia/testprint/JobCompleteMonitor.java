package com.insomnia.testprint;

import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

public class JobCompleteMonitor extends PrintJobAdapter {
	private boolean completed = false;

	@Override
	    public void printJobCanceled(PrintJobEvent pje) {
	    signalCompletion();
	}

	@Override
	    public void printJobCompleted(PrintJobEvent pje) {
	    signalCompletion();
	}

	@Override
	    public void printJobFailed(PrintJobEvent pje) {
	    signalCompletion();
	}

	@Override
	    public void printJobNoMoreEvents(PrintJobEvent pje) {
	    signalCompletion();
	}

	private void signalCompletion() {

	    synchronized (JobCompleteMonitor.this) {

		completed = true;

		JobCompleteMonitor.this.notify();

	    }

	}

	public synchronized void waitForJobCompletion() {

	    try {

		while (!completed) {

		    wait();

		}

	    } catch (InterruptedException e) {

	    }
	}
}
