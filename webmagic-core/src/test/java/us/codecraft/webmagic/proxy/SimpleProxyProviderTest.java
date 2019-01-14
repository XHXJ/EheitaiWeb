package us.codecraft.webmagic.proxy;

import org.junit.Test;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author code4crafter@gmail.com
 *         Date: 17/4/16
 *         Time: 上午10:29
 */
public class SimpleProxyProviderTest {

    public static final Task TASK = Site.me().toTask();

    @Test
    public void test_get_proxy() throws Exception {
        List<Proxy> proxies = new ArrayList<Proxy>();
        proxies.add(new Proxy("127.0.0.1", 1087));
        proxies.add(new Proxy("127.0.0.1", 1088));
        SimpleProxyProvider proxyProvider = SimpleProxyProvider.from(proxies);
        Proxy proxy = proxyProvider.getProxy(TASK);
        assertThat(proxy).isEqualTo(proxies.get(0));
        proxy = proxyProvider.getProxy(TASK);
        assertThat(proxy).isEqualTo(proxies.get(1));
        proxy = proxyProvider.getProxy(TASK);
        assertThat(proxy).isEqualTo(proxies.get(0));
    }
}
