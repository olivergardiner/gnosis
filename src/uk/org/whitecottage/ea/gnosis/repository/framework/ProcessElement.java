package uk.org.whitecottage.ea.gnosis.repository.framework;

import java.util.List;

import uk.org.whitecottage.ea.gnosis.jaxb.framework.Parent;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Predecessor;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Process;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.ProcessInstance;

public class ProcessElement {
	protected ProcessInstance instance;
	protected Process process;
	protected int start;
	protected int end;

	public ProcessElement(ProcessInstance instance, Process process) {
		super();
		this.process = process;
		this.instance = instance;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public List<Parent> getParent() {
		return instance.getParent();
	}

	public List<Predecessor> getPredecessor() {
		return instance.getPredecessor();
	}

	public String getName() {
		return process.getName();
	}

	public boolean hasProcessId(String processId) {
		return processId.equals(process.getProcessId());
	}

	public String getDuration() {
		return instance.getDuration();
	}

	public Process getProcess() {
		return process;
	}
}
