package uk.org.whitecottage.ea.gnosis.repository;

import java.util.List;
import java.util.logging.Logger;

import uk.org.whitecottage.ea.gnosis.jaxb.framework.Framework;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Parent;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Predecessor;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Process;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.ProcessDomain;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.ProcessFlow;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.ProcessInstance;
import uk.org.whitecottage.ea.gnosis.json.JSONArray;
import uk.org.whitecottage.ea.gnosis.json.JSONBoolean;
import uk.org.whitecottage.ea.gnosis.json.JSONMap;
import uk.org.whitecottage.ea.gnosis.json.JSONString;

public class ProcessFlows extends FrameworkProcessor {
	//@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.framework");

	public ProcessFlows(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot, context);
	}

	public String getJSON() {
		Framework framework = loadFramework();
		
		JSONMap businessProcesses = new JSONMap();
		JSONArray processDomains = new JSONArray("processDomains");
		businessProcesses.put(processDomains);

		for (ProcessDomain domain: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessDomain()) {
			processDomains.add(renderProcessDomain(domain));
		}

		JSONArray processFlows = new JSONArray("processFlows");
		businessProcesses.put(processFlows);
		
		for (ProcessFlow flow: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow()) {
			processFlows.add(renderProcessFlow(flow));
		}

		return businessProcesses.toJSON();
	}

	protected JSONMap renderProcessDomain(ProcessDomain domain) {
		JSONMap domainJSON = new JSONMap();
		
		JSONString idJSON = new JSONString("id", domain.getDomainId());
		domainJSON.put(idJSON);
				
		JSONString nameJSON = new JSONString("name", domain.getName());
		domainJSON.put(nameJSON);
				
		JSONString valueChainJSON = new JSONString("valueChain", domain.getName());
		domainJSON.put(valueChainJSON);
				
		JSONString descriptionJSON = new JSONString("description", domain.getDescription());
		domainJSON.put(descriptionJSON);
		
		JSONArray processesJSON = new JSONArray("processes");
		domainJSON.put(processesJSON);
								
		for (Process process: domain.getProcess()) {
			processesJSON.add(renderProcess(process));
		}
		
		return domainJSON;
	}
	
	protected JSONMap renderProcess(Process process) {
		JSONMap processJSON = new JSONMap();
		
		JSONString idJSON = new JSONString("id", process.getProcessId());
		processJSON.put(idJSON);
				
		JSONString nameJSON = new JSONString("name", process.getName());
		processJSON.put(nameJSON);
				
		JSONString descriptionJSON = new JSONString("description", process.getDescription());
		processJSON.put(descriptionJSON);
								
		return processJSON;
	}

	protected JSONMap renderProcessFlow(ProcessFlow processFlow) {
		JSONMap processJSON = new JSONMap();
		
		JSONString nameJSON = new JSONString("name", processFlow.getName());
		processJSON.put(nameJSON);
				
		JSONString idJSON = new JSONString("id", processFlow.getFlowId());
		processJSON.put(idJSON);
				
		JSONArray instancesJSON = new JSONArray("instances");
		processJSON.put(instancesJSON);
		
		for (ProcessInstance instance: processFlow.getProcessInstance()) {
			instancesJSON.add(renderProcessInstance(instance));
		}
		
		return processJSON;
	}
	
	protected JSONMap renderProcessInstance(ProcessInstance instance) {
		JSONMap instanceJSON = new JSONMap();
		
		JSONString idJSON = new JSONString("process", instance.getProcessId());
		instanceJSON.put(idJSON);
		
		if (instance.getDuration() != null) {
			JSONString durationJSON = new JSONString("duration", instance.getDuration());
			instanceJSON.put(durationJSON);
		}

		JSONArray parentsJSON = new JSONArray("parents");
		instanceJSON.put(parentsJSON);
		
		for (Parent parent: instance.getParent()) {
			JSONString parentJSON = new JSONString(parent.getProcess());
			parentsJSON.add(parentJSON);
		}
		
		JSONArray predecessorsJSON = new JSONArray("predecessors");
		instanceJSON.put(predecessorsJSON);
		
		for (Predecessor predecessor: instance.getPredecessor()) {
			JSONMap predecessorJSON = new JSONMap();
			predecessorsJSON.add(predecessorJSON);

			JSONString processJSON = new JSONString("predecessor", predecessor.getProcess());
			predecessorJSON.put(processJSON);
			
			if (predecessor.isContiguous() != null && !predecessor.isContiguous()) {
				predecessorJSON.put(new JSONBoolean("contiguous", false));
			} else {
				predecessorJSON.put(new JSONBoolean("contiguous", true));
			}
		}
		
		return instanceJSON;
	}

	public void addFlow(String flowId) {
		log.info("Adding flow: " + flowId);
		
		Framework framework = loadFramework();

		ProcessFlow flow = new ProcessFlow();
		flow.setFlowId(flowId);
		flow.setName("New Process Flow");
		framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow().add(flow);
		
		saveFramework(framework);
	}

	public void copyFlow(String flowId, String copyId) {
		log.info("Renaming flow: " + flowId);
		
		Framework framework = loadFramework();

		for (ProcessFlow flow: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow()) {
			if (flow.getFlowId().equals(flowId)) {
				framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow().add(copyFlow(flow, copyId));
				break;
			}
		}
		
		saveFramework(framework);
	}
	
	protected ProcessFlow copyFlow(ProcessFlow flow, String copyId) {
		ProcessFlow newFlow = new ProcessFlow();
		newFlow.setFlowId(copyId);
		newFlow.setName("Copy of " + flow.getName());
		
		for (ProcessInstance instance: flow.getProcessInstance()) {
			ProcessInstance newInstance = new ProcessInstance();
			newInstance.setProcessId(instance.getProcessId());;
			newInstance.setDuration(instance.getDuration());
			
			for (Parent parent: instance.getParent()) {
				Parent newParent = new Parent();
				newParent.setProcess(parent.getProcess());
				
				newInstance.getParent().add(newParent);
			}
			
			for (Predecessor predecessor: instance.getPredecessor()) {
				Predecessor newPredecessor = new Predecessor();
				newPredecessor.setContiguous(predecessor.isContiguous());
				newPredecessor.setProcess(predecessor.getProcess());
				
				newInstance.getPredecessor().add(newPredecessor);
			}
			
			newFlow.getProcessInstance().add(newInstance);
		}
		
		return newFlow;
	}

	public void renameFlow(String flowId, String name) {
		log.info("Renaming flow: " + flowId + ", " + name);

		Framework framework = loadFramework();

		for (ProcessFlow flow: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow()) {
			if (flow.getFlowId().equals(flowId)) {
				flow.setName(name);
			}
		}
		
		saveFramework(framework);
	}

	public void deleteFlow(String flowId) {
		log.info("Renaming flow: " + flowId);

		Framework framework = loadFramework();

		for (ProcessFlow flow: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow()) {
			if (flow.getFlowId().equals(flowId)) {
				framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow().remove(flow);
				break;
			}
		}
		
		saveFramework(framework);
	}

	public void updateProcessInstance(String flowId, String instanceId, String duration, String mode) {
		log.info("Updating process instance: " + flowId + ", " + instanceId + ", " + duration + ", " + mode);

		Framework framework = loadFramework();

		for (ProcessFlow flow: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow()) {
			if (flow.getFlowId().equals(flowId)) {
				if (mode.equals("add")) {
					ProcessInstance instance = new ProcessInstance();
					instance.setProcessId(instanceId);
					instance.setDuration(duration);
					flow.getProcessInstance().add(instance);
				} else {
					for (ProcessInstance instance: flow.getProcessInstance()) {
						if (instance.getProcessId().equals(instanceId)) {
							instance.setDuration(duration);
						}
					}
				}
			}
		}
		
		saveFramework(framework);
	}

	public void moveProcessInstance(String flowId, String instanceId, int position) {
		log.info("Moving process instance: " + flowId + ", " + instanceId + ", " + position);

		Framework framework = loadFramework();

		for (ProcessFlow flow: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow()) {
			if (flow.getFlowId().equals(flowId)) {
				for (ProcessInstance instance: flow.getProcessInstance()) {
					if (instance.getProcessId().equals(instanceId)) {
						flow.getProcessInstance().remove(instance);
						flow.getProcessInstance().add(position, instance);
						break;
					}
				}
			}
		}
		
		saveFramework(framework);
	}

	public void addParentDependency(String flowId, String instanceId, String parentId) {
		log.info("Updating parent dependency: " + flowId + ", " + instanceId + ", " + parentId);

		Framework framework = loadFramework();

		for (ProcessFlow flow: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow()) {
			if (flow.getFlowId().equals(flowId)) {
				for (ProcessInstance instance: flow.getProcessInstance()) {
					if (instance.getProcessId().equals(instanceId)) {
						boolean found = false;
						for (Parent parent: instance.getParent()) {
							if (parent.getProcess().equals(parentId)) {
								found = true;
								
								break;
							}
						}
						
						if (!found) {
							Parent newParent = new Parent();
							newParent.setProcess(parentId);
							instance.getParent().add(newParent);
						}
						
						break;
					}
				}
				
				break;
			}
		}
		
		saveFramework(framework);
	}

	public void updatePredecessorDependency(String flowId, String instanceId, String predecessorId, String contiguous) {
		log.info("Updating predecessor dependency: " + flowId + ", " + instanceId + ", " + predecessorId);
		if (predecessorId == null) {
			predecessorId = "";
		}

		Framework framework = loadFramework();

		for (ProcessFlow flow: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow()) {
			if (flow.getFlowId().equals(flowId)) {
				for (ProcessInstance instance: flow.getProcessInstance()) {
					if (instance.getProcessId().equals(instanceId)) {
						boolean found = false;
						for (Predecessor predecessor: instance.getPredecessor()) {
							if (predecessor.getProcess().equals(predecessorId)) {
								found = true;
								predecessor.setContiguous(new Boolean(contiguous.equals("true")));
								
								break;
							}
						}
						
						if (!found) {
							Predecessor newPredecessor = new Predecessor();
							newPredecessor.setProcess(predecessorId);
							newPredecessor.setContiguous(new Boolean(contiguous.equals("true")));
							instance.getPredecessor().add(newPredecessor);
						}
						
						break;
					}
				}
				
				break;
			}
		}
		
		saveFramework(framework);
	}

	public void deleteItem(String flowId, String instanceId, String dependencyId, String type) {
		log.info("Deleting item: " + flowId + ", " + instanceId + ", " + dependencyId + ", " + type);

		Framework framework = loadFramework();

		List<ProcessFlow> flows =  framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow();
		for (ProcessFlow flow: flows) {
			if (flow.getFlowId().equals(flowId)) {
				if (type.equals("flow")) {
					flows.remove(flow);
				} else {
					for (ProcessInstance instance: flow.getProcessInstance()) {
						if (instance.getProcessId().equals(instanceId)) {
							if (type.equals("instance")) {
								log.info("removing instance");
								flow.getProcessInstance().remove(instance);
							} else {
								if (type.equals("parent")) {
									for (Parent parent: instance.getParent()) {
										if (parent.getProcess().equals(dependencyId)) {
											instance.getParent().remove(parent);
											break;
										}
									}
								} else if (type.equals("predecessor")) {
									for (Predecessor predecessor: instance.getPredecessor()) {
										if (predecessor.getProcess().equals(dependencyId)) {
											instance.getPredecessor().remove(predecessor);
											break;
										}
									}
								}
							}
							break;
						}
					}
				}
				break;
			}
		}
		
		saveFramework(framework);
	}
}
