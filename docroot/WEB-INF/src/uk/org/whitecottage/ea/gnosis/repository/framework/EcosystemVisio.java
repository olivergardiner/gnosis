package uk.org.whitecottage.ea.gnosis.repository.framework;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import uk.org.whitecottage.ea.gnosis.repository.FrameworkProcessor;
import uk.org.whitecottage.visio.Page;
import uk.org.whitecottage.visio.Shape;
import uk.org.whitecottage.visio.Visio;

public class EcosystemVisio extends FrameworkProcessor {
	
	@SuppressWarnings("unused")
	private static final Log log = LogFactoryUtil.getLog(EcosystemVisio.class);

	public EcosystemVisio(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot, context);

		loadFramework();
	}

	public void render(Visio visio) {
		//ShapeType shape = visio.addBox(visio.getPage(0), 50, 50, 100, 20);
		//shape.getTextOrXFormOrLine().add(visio.createText("Hello"));
		//shape.getTextOrXFormOrLine().add(visio.createTextBlock(0));
		Page page = visio.getPage(0);
		
		Shape box = Shape.createBox(50/25.4, 50/25.4, 100/25.4, 20/25.4);
		box.setID(1);
		
		page.addShape(box);
	}
}
