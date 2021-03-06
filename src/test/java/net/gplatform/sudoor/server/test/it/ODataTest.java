package net.gplatform.sudoor.server.test.it;

/*
 * #%L
 * sudoor-server-lib
 * %%
 * Copyright (C) 2013 - 2014 Shark Xu
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.gplatform.sudoor.server.Application;

import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.junit.BeforeClass;
import org.junit.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@EnableTransactionManagement
@IntegrationTest
public class ODataTest {


	public final String ODATA_SERVICE_URL = "http://localhost:8080/sudoor-server-lib/data/odata.svc";

	static Client client = null;

	@BeforeClass
	public static void  init() {
		client = ClientBuilder.newBuilder().build();
	}

	@Test
	public void retrieveODataMetaData() {
		WebTarget target = client.target(ODATA_SERVICE_URL).path("/$metadata");
		Response response = target.request(MediaType.APPLICATION_XML_TYPE).get();
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);

		System.out.println("retrieveODataMetaData() statusCode:" + statusCode);
		System.out.println("retrieveODataMetaData() content:" + content);
		assert (statusCode == 200);
	}

}
