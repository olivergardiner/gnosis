package uk.org.whitecottage.ea.gnosis.repository.applications;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import uk.org.whitecottage.ea.gnosis.jaxb.applications.Application;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Migration;

public class ApplicationGroup {
	protected List<Application> applications;
	protected String id;
	protected String name;

	public ApplicationGroup() {
		applications = new ArrayList<Application>();
	}
	
	public List<Application> getApplications() {
		return applications;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Application> build(List<Application> source, List<Application> singletons, Application app) {
		List<Migration> migrations = app.getMigration();
		Iterator<Migration> m = migrations.iterator();
		while (m.hasNext()) {
			String appId = m.next().getTo();
			Application a = findApplication(appId, source);
			if (a != null) {
				applications.add(a);
				source.remove(a);
			} else {
				Application s = findApplication(appId, singletons);
				if (s != null) {
					applications.add(s);
					singletons.remove(s);
				}
			}
		}

		applications.add(app);
		source.remove(app);

		return applications;
	}

	public List<ApplicationGroup> split(int pagination) {
		List<ApplicationGroup> splitGroups = new ArrayList<ApplicationGroup>();

		int count = 0;
		ApplicationGroup g = new ApplicationGroup();
		Iterator<Application> a = applications.iterator();
		g.setId(id);
		g.setName(name);
		while (a.hasNext()) {
			if (count == pagination) {
				splitGroups.add(g);
				count = 0;
				g = new ApplicationGroup();
				g.setId(id);
				g.setName(name);
			}
			Application app = a.next();
			if (!g.contains(app)) {
				List<Migration> migrations = app.getMigration();
				Iterator<Migration> m = migrations.iterator();
				int mcount = 0;
				while (m.hasNext()) {
					String to = m.next().getTo();
					Application target = findApplication(to, applications);
					if (target != null && !g.contains(target)) {
						mcount++;
					}
				}
				if (count + mcount > pagination) {
					splitGroups.add(g);
					count = 0;
					g = new ApplicationGroup();
					g.setId(id);
					g.setName(name);
				}
				g.add(app);
				count++;
				m = migrations.iterator();
				while (m.hasNext()) {
					String to = m.next().getTo();
					Application target = findApplication(to, applications);
					if (target != null && !g.contains(target)) {
						g.add(target);
						count++;
					}
				}
			}
		}
		
		if (count != 0) {
			splitGroups.add(g);
		}
		
		return splitGroups;
	}
	
	protected Application findApplication(String appId, List<Application> applicationList) {
		Iterator<Application> a = applicationList.iterator();
		while (a.hasNext()) {
			Application app = a.next();
			if (app.getAppId().equals(appId)) {
				return app;
			}
		}
		
		return null;
	}
	
	public boolean add(Application arg0) {
		return applications.add(arg0);
	}

	public void add(int arg0, Application arg1) {
		applications.add(arg0, arg1);
	}

	public boolean addAll(Collection<? extends Application> arg0) {
		return applications.addAll(arg0);
	}

	public boolean addAll(int arg0, Collection<? extends Application> arg1) {
		return applications.addAll(arg0, arg1);
	}

	public void clear() {
		applications.clear();
	}

	public boolean contains(Object arg0) {
		return applications.contains(arg0);
	}

	public Application get(int arg0) {
		return applications.get(arg0);
	}

	public int indexOf(Object arg0) {
		return applications.indexOf(arg0);
	}

	public boolean isEmpty() {
		return applications.isEmpty();
	}

	public Iterator<Application> iterator() {
		return applications.iterator();
	}

	public int lastIndexOf(Object arg0) {
		return applications.lastIndexOf(arg0);
	}

	public Application remove(int arg0) {
		return applications.remove(arg0);
	}

	public boolean remove(Object arg0) {
		return applications.remove(arg0);
	}

	public boolean removeAll(Collection<?> arg0) {
		return applications.removeAll(arg0);
	}

	public int size() {
		return applications.size();
	}
}
