package com.greenstarnetwork.test.services.cloudmanager;

import java.util.List;

import org.junit.Test;

import com.greenstarnetwork.services.cloudmanager.DefaultCloudManagerProvider;
import com.greenstarnetwork.services.cloudmanager.ICloudManagerProvider;
import com.greenstarnetwork.services.cloudmanager.model.Host;

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
