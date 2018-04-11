package mockito;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class HelloWorld {

	@Mock
	private List mockedList;

	public HelloWorld() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * 1、验证行为
	 */
	@Test
	public void verify_behaviour() {
		List list = Mockito.mock(List.class);
		list.add(1);
		list.add(2);
		Mockito.verify(list).add(1);
		Mockito.verify(list).add(2);
	}

	/**
	 * 2、模拟预期结果
	 */
	@Test
	public void when_then_return() {
		Iterator mock = Mockito.mock(Iterator.class);
		Mockito.when(mock.next()).thenReturn("1").thenReturn("2");

		Assert.assertEquals("1", mock.next());
		Assert.assertEquals("2", mock.next());
		Assert.assertEquals("2", mock.next());
	}

	@Test(expected = Exception.class)
	public void when_then_throw() throws Exception {
		OutputStream os = Mockito.mock(OutputStream.class);
		OutputStreamWriter osw = new OutputStreamWriter(os);
		Mockito.doThrow(Exception.class).when(os).close();
		osw.close();
	}

	/**
	 * 3、未打桩时返回类型默认值，并进行友好提示
	 */
	@Test
	public void returns_smart_nulls() {
		List mock = Mockito.mock(List.class, Mockito.RETURNS_SMART_NULLS);
		System.out.println(mock.get(0));
	}

	/**
	 * 4、使用mock注解
	 */
	@Test
	public void annotation_mock() {
		mockedList.getClass();
	}
}
