package uk.org.whitecottage.ea.gnosis.repository.framework;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import uk.org.whitecottage.ea.gnosis.repository.FrameworkProcessor;
import uk.org.whitecottage.visio.Visio;

public class EcosystemVisio extends FrameworkProcessor {
	
	@SuppressWarnings("unused")
	private static final Log log = LogFactoryUtil.getLog(EcosystemVisio.class);

	public EcosystemVisio(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot, context);

		loadFramework();
	}

	public void render(Visio visio) {
		visio.addBox(visio.getPage(0), 50, 50, 10, 10);
	}
}
