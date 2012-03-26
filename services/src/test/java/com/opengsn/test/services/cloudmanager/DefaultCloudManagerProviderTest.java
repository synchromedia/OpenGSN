package com.opengsn.test.services.cloudmanager;

import java.util.List;

import org.junit.Test;

import com.opengsn.services.cloudmanager.DefaultCloudManagerProvider;
import com.opengsn.services.cloudmanager.ICloudManagerProvider;
import com.opengsn.services.cloudmanager.model.Host;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
public class DefaultCloudManagerProviderTest {
	ICloudManagerProvider provider = new DefaultCloudManagerProvider(null,null);
	@Test
	public void testListHosts() {
		List<Host> hosts = provider.listAllHosts();
		System.out.println(hosts);
	}


}
