gnosis
======

An Enterprise Architecture repository based on a Java Portal and an XMLDB

As the underlying repository is based on XML, extension to the metamodel is relatively straightforward, however the underlying metamodel is not infinitely configurable but has been designed to support the key domains of interest to an Enterprise Architect. The reason for this is twofold: 1) to make it easier and quicker to write functional code that is tailored to the metamodel, and 2) to provide a tool that can be used out of the box for the purpose for which it was intended.

NB: Gnosis does not seek to implement or align to any predefined EA "framework" - you won't find any references to Zachman or TOGAF. The reason for this is that such frameworks often suffer (to varying degrees) from a number of weaknesses including: incompleteness, excessive complexity, boiling the ocean and a general lack of pragmatism. Instead, Gnosis is intended to be a simple but effective EA repository born out of practical experience.

In broad terms, the goal of Gnosis is to cover the key domains of Business Process, Business Data and Business Applications to a level that is appropriate to an Enterprise Architect. That means that Gnosis is explicitly not intended to support: Applications Management, Project Management, Service Integration and Management, Product Management, Business Process Management,Portfolio Management or SOA Governance. That said, the underlying metamodel has touch-points with all of these disciplines and should be able to be used as a unifying framework for a wide range of IT functions.

From a modelling perspective, simple schemas have been designed to represent an Enterprise Architect's logical view of business processes and applications. In the data domain, however, the power of UML is leveraged to provide an Entity Relationship paradigm with tool support through the Eclipse Papyrus project.

Gnosis is currently written to run in the Liferay Portal and the eXist XMLDB. Porting to another XMLDB should be relatively straightforward, albeit untested. Moving to another portal framework will harder as JSR168/286 portals struggle to provide a seamless approach to (in particular) client-side behaviour controlled through Javascript and CSS.
