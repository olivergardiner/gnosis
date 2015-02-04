gnosis
======

An Enterprise Architecture repository based on a Java Portal and an XMLDB

As the underlying repository is based on XML, extension to the metamodel is relatively straightforward, however the underlying metamodel is not infinitely configurable but has been designed to support the key domains of interest to an Enterprise Architect. The reason for this is twofold: 1) to make it easier and quicker to write functional code that is tailored to the metamodel, and 2) to provide a tool that can be used out of the box for the purpose for which it was intended.

NB: Gnosis does not seek to implement or align to any predefined EA "framework" - you won't find any references to Zachman or TOGAF. The reason for this is that such frameworks often suffer (to varying degrees) from a number of weaknesses including: incompleteness, excessive complexity, boiling the ocean and a general lack of pragmatism. Instead, Gnosis is intended to be a simple but effective EA repository born out of practical experience.

In broad terms, the goal of Gnosis is to cover the key domains of Business Process, Business Data and Business Applications to a level that is appropriate to an Enterprise Architect. That means that Gnosis is explicitly not intended to support: Applications Management, Project Management, Service Integration and Management, Product Management, Business Process Management,Portfolio Management or SOA Governance. That said, the underlying metamodel has touch-points with all of these disciplines and should be able to be used as a unifying framework for a wide range of IT functions.

From a modelling perspective, simple schemas have been designed to represent an Enterprise Architect's logical view of business processes and applications. In the data domain, however, the power of UML is leveraged to provide an Entity Relationship paradigm with tool support through the Eclipse Papyrus project.

Gnosis is currently written to run in the Liferay Portal and the eXist XMLDB. Porting to another XMLDB should be relatively straightforward, albeit untested. Moving to another portal framework will harder as JSR168/286 portals struggle to provide a seamless approach to (in particular) client-side behaviour controlled through Javascript and CSS.

Build instructions
==================

Because Gnosis is designed to run within Liferay, by far the easiest way to get started with Gnosis is by using the standard Liferay development set up. This is described in detail on liferay.com (http://www.liferay.com/en_GB/documentation/liferay-portal/6.2/development/-/ai/developing-apps-with-liferay-ide-liferay-portal-6-2-dev-guide-02-en) butI have found it to require a few tweaks:

The specific setup I have used is based on Eclipse Luna and Liferay CE 6.2 ga3, so the first step is to install Eclipse and add the Liferay IDE extension. You will also need to download the Liferay portal runtime (I recommend the Tomcat bundle) and the SDK. Optionally, you can also download the portal doc and src bundles. The portal runtime and the SDK should be unzipped next to each other. In Eclipse, configure a Liferay server with the home directory of the portal runtime you just unzipped. If you use the default server name, the project settings in the gnosis Eclipse project will automatically pick up the right server libraries.

The next step is to clone (or download) the project files from GitHub - the important point is to make sure that the project directory is placed under the SDK/portlets directory as the build system (based on Ant) is dependent on scripts within the SDK. Do not try and clone the project into an Eclipse workspace or into a general GitHub local directory as the project won't build properly. When you've finished, you should have a directory layout something like this:

## Folder structure
    + + liferay-plugins-sdk-6.2   (I prefer to manually add "-ce-ga3" on the end to avoid confusion)
    | |
    | + ...
    | |
    | + + portlets
    | | |
    | | + + gnosis
    | | |
    | | + - ...
    | |
    | + - ...
    |
    + + liferay-portal-6.2-ce-ga3
    |
    + - liferay-plugins-sdk-6.2-ce-ga3-20150103155803016.zip
    |
    + - liferay-portal-doc-6.2-ce-ga3-20150121124737370.zip
    |
    + - liferay-portal-src-6.2-ce-ga3-20150103155803016.zip
    |
    + - liferay-portal-tomcat-6.2-ce-ga3-20150103155803016.zip

Once you have you files set up like this then there are the a couple of minor edits to the build system that I have found to be necessary:

1. Edit build.properties in the SDK root directory and change the following lines (from line 149).

from:

##
## Compiler
##

    ant.build.javac.source=1.6
    ant.build.javac.target=1.6

    #javac.compiler=com.google.errorprone.ErrorProneAntCompilerAdapter
    #javac.compiler=modern
    javac.compiler=org.eclipse.jdt.core.JDTCompilerAdapter

to:

##
## Compiler
##

    ant.build.javac.source=1.8
    ant.build.javac.target=1.8

    #javac.compiler=com.google.errorprone.ErrorProneAntCompilerAdapter
    javac.compiler=modern
    #javac.compiler=org.eclipse.jdt.core.JDTCompilerAdapter

NB: You should be fine to use Java 7, in which case change "1.8" above to "1.7"

2. Edit build-common.xml in the SDK root directory and change the following lines (from line 1742).

from:

			<if>
				<equals arg1="${module.dir}" arg2="." />
				<then>
					<var name="plugin.name" value="${ant.project.name}" />
				</then>
				<else>
					<antelope:grep
						group="1"
						in="@{module.dir}"
						property="plugin.name"
						regex="(?:.*[/\\])(.*)"
					/>
				</else>
			</if>

to:
			
			<var name="plugin.name" value="${ant.project.name}" />

I have found this latter step to be necessary because the rest of the build scripts need the ant property plugin.name to end in "-portlet" but the runtime invocation of the Ant macro substitutes the full path for "." and so the test always fails.

The final point to note with regard to the build process is that Ivy is used to manage some remote dependencies and you may need to do a certain amount of proxy configuration if you're sitting behind a corporate firewall. I ended up performing the setup on a machine just sitting behind a NAT firewall and then copying the entire resulting SDK directory once Ivy had fetched the dependencies - once they are present, Ivy won't try to fetch them again.

At this point, if all has gone according to plan you should be in a position to start the local Liferay server and go through the standard Liferay configuration of database and users etc.. Once you have a running server and have done the basic setup to your liking you can test the Gnosis deployment by simply running the project default ant task (deploy) from build.xml - if all goes well you should see log messages telling you that a number of portlets have been successfully deployed and they should also be visible in the Liferay control panel under the category "Gnosis".

So we're all set? Well unfortunately not quite - we have portlets but as yet no data for them to work on. We need to first configure Gnosis and then set up an instance of Exist and populate it with a clean data set. Currently Gnosis does not detect an unconfigured instance and automatically deploy default configuration so you need to copy the entire project directory structure under docroot/WEB-INF/init/data to the data directory in the portal root directory. The default configuration expects the repository to be running on the same tomcat instance as servlet named "exist".

Setting up exist is not complicated - it is essentially just a case of deploying an appropriately named war file into webapps directory of the Tomcat instance (which is inside the portal root). The one subtlety is that you need to configure eclipse to know where you want its data files to be stored and you also need the XMLDB to have the right collection structure and base data.

At this point we should reflect on the statement above that we really want a "clean" data set rather than an "empty" data set and so there is a separate project on GitHub called "gnosis-data" which delivers a clean data set for gnosis based on some standard sources: Porter for the value chain definition and APQC for the process taxonomy, etc.. In this project you will find similarly detailed instructions for setting up exist so that it has a suitable starting data set for Gnosis.

If properly configured, Gnosis should now be functional but to be able to use it you need to create a site in Liferay with an appropriate layout of content pages into which the gnosis portlets can be placed. Again, this isn't difficult but does mean spending a bit of time getting to grips with Liferay administration. To shortcut this process, there is a third project on GitHub called "gnosis-portal" which is Liferay theme project. This not only defines a custom look and feel for Gnosis but also provides a mechanism for defining a site template and including sample content so that a complete set of pages and content can be easily applied and then customised if desired.
