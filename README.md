# gnosis
An Enterprise Architecture modelling tool built using Eclipse Sirius

This project originally started as an EA repository built in Java using a range of different technologies. The first experiments used JSR 286 portlets, first using Liferay and then Gatein. While the portals brought some nice features, overall UX and performance was a bit old school, and making all the JS libraries play together to make all the widgets was a pain. I then moved to Vaadin which made things a bit easier - but the reality was that there was still too much effort on the UI side and not enough on the underlying purpose.

This iteration represents a complete shift and is now a client side tool running in the Eclipse RCP using Sirius. This is providing to be a very effective way of developing. Being based on EMF, I also get a set of classes generated that allow me to manipulate the model programmatically with ease.
