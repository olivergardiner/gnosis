gnosis
==============

Gnosis is a simple Enterprise Architecture repository tool that is built as a Vaadin application. For persistence, the eXist XMLDB is used (although any XMLDB compliant storage could be used) By default, it is assumed that both components are hosted on the same Servlet 3.0 container (such as Tomcat 7+)

Project Structure
=================

The project consists of the following three modules:

- parent project: common metadata and configuration
- gnosis-widgetset: widgetset, custom client side code and dependencies to widget add-ons
- gnosis-ui: main application module, development time
- gnosis-production: module that produces a production mode WAR for deployment

The production mode module recompiles the widgetset (obfuscated, not draft), activates production mode for Vaadin with a context parameter in web.xml and contains a precompiled theme. The ui module WAR contains an unobfuscated widgetset, and is meant to be used at development time only.

Setting up Nexus
================

The build for Gnosis is based on maven and is largely able to obtain dependencies from Maven Central. However, the eXist and XMLDB jars are not yet released through Maven Central and so the build environment must be configured to provide them locally. They can be directly inserted into your local maven cache (~/.m2/repository) but a cleaner option is to use a local maven repository such as Nexus.

Once Nexus has been installed, maven needs to be configured to use the Nexus repository as a mirror for externally hosted repositories (such as Maven Central) and also to deploy artifacts to Nexus. This is controlled by the file settings.xml and is most conveniently managed by placing the desired settings.xml in the local maven user directory (~/.m2). A clean settings.xml that is appropriately configured using Nexus defaults is provided in the root of this project.

By default, Nexus hosts a repository for local releases (maven-releases) which, along with the mirror of maven-central is included within the group maven-public. In order to build Gnosis, the missing jars from the exist project need to be deployed to the local Nexus repository under maven-releases. To do this you will need to have maven installed with the bin directory on your path. The general form of the command is:

mvn deploy:deploy-file -DgroupId=<group-id> \
  -DartifactId=<artifact-id> \
  -Dversion=<version> \
  -Dpackaging=<type-of-packaging> \
  -Dfile=<path-to-file> \
  -DrepositoryId=<id-to-map-on-server-section-of-settings.xml> \
  -Durl=<url-of-the-repository-to-deploy>

To deploy to the local Nexus repository using the provided settings.xml, the repositoryId must be "nexus", as configured under the <servers> section.

Download and unpack the eXist 2.2 distribution. Assuming that you are in the top-level directory of that distribution, the two jars needed are:
  exist.jar
  lib/core/xmldb.jar

The should be deployed to Nexus using the commands:

mvn deploy:deploy-file -DgroupId=org.exist-db -DartifactId=exist -Dversion=2.2 -Dpackaging=jar -Dfile=exist.jar -DrepositoryId=nexus -Durl=http://127.0.0.1:8081/repository/maven-releases/

mvn deploy:deploy-file -DgroupId=org.xmldb -DartifactId=xmldb -Dversion=1.0 -Dpackaging=jar -Dfile=lib\core\xmldb.jar -DrepositoryId=nexus -Durl=http://127.0.0.1:8081/repository/maven-releases/

Once this has been set up correctly, the project should now build under eclipse. It may still be necessary to refresh the maven dependencies in eclipse before the build path errors are resolved.

Workflow
========

To compile the entire project, run "mvn install" in the parent project.

Other basic workflow steps:

- getting started
- compiling the whole project
  - run "mvn install" in parent project
- developing the application
  - edit code in the ui module
  - run "mvn jetty:run" in ui module
  - open http://localhost:8080/
- client side changes or add-ons
  - edit code/POM in widgetset module
  - run "mvn install" in widgetset module
  - if a new add-on has an embedded theme, run "mvn vaadin:update-theme" in the ui module
- debugging client side code
  - run "mvn vaadin:run-codeserver" in widgetset module
  - activate Super Dev Mode in the debug window of the application
- creating a production mode war
  - run "mvn -Pproduction package" in the production mode module or in the parent module
- testing the production mode war
  - run "mvn -Pproduction jetty:run-war" in the production mode module


Developing a theme using the runtime compiler
-------------------------

When developing the theme, Vaadin can be configured to compile the SASS based
theme at runtime in the server. This way you can just modify the scss files in
your IDE and reload the browser to see changes.

To use on the runtime compilation, open pom.xml of your UI project and comment 
out the compile-theme goal from vaadin-maven-plugin configuration. To remove 
an existing pre-compiled theme, remove the styles.css file in the theme directory.

When using the runtime compiler, running the application in the "run" mode 
(rather than in "debug" mode) can speed up consecutive theme compilations
significantly.

The production module always automatically precompiles the theme for the production WAR.

Using Vaadin pre-releases
-------------------------

If Vaadin pre-releases are not enabled by default, use the Maven parameter
"-P vaadin-prerelease" or change the activation default value of the profile in pom.xml .
