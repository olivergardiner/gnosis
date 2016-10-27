/*
 * Copyright 2015 Explicatis GmbH <ext-token-field@explicatis.com>
 * 
 * Author: Florian Schmitt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.explicatis.ext_token_field.shared;

import com.explicatis.ext_token_field.ExtTokenField;
import com.explicatis.ext_token_field.client.ExtTokenFieldWidget;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(ExtTokenField.class)
public class ExtTokenFieldConnector extends AbstractFieldConnector
{

	private ExtTokenFieldServerRpc serverRpc = RpcProxy.create(ExtTokenFieldServerRpc.class, this);

	public ExtTokenFieldConnector()
	{
	}

	@Override
	public ExtTokenFieldWidget getWidget()
	{
		return (ExtTokenFieldWidget) super.getWidget();
	}

	@Override
	protected void init()
	{
		getWidget().setServerRpc(serverRpc);
	}

	@Override
	public ExtTokenFieldState getState()
	{
		return (ExtTokenFieldState) super.getState();
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent)
	{
		super.onStateChanged(stateChangeEvent);
		getWidget().setApplicationConnection(getConnection());

		for (TokenAction a : getState().tokenActions)
		{
			String resourceUrl = getResourceUrl(a.identifier + "-icon");
			if (resourceUrl != null && !resourceUrl.isEmpty())
			{
				getWidget().setIconResourceUrl(a, resourceUrl);
			}
		}

		getWidget().setReadOnly(isReadOnly());
		getWidget().updateTokens(getState().tokens);
	}
}
