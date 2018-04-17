package mockito;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class HelloWorld {

	@Mock
	private List mockedList;

	public HelloWorld() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * 1、验证行为
	 * verify
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
	 * when thenReturn
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
	 * RETURNS_SMART_NULLS
	 * RETURNS_DEEP_STUBS
	 */
	@Test
	public void returns_smart_nulls() {
		List mock = Mockito.mock(List.class, Mockito.RETURNS_SMART_NULLS);
		System.out.println(mock.get(0));
	}

	@Test
	public void returns_deep_stubs() {
		Account account = Mockito.mock(Account.class, Mockito.RETURNS_DEEP_STUBS);
		Mockito.when(account.getRailwayTicket().getDestination()).thenReturn("xiamen");
		account.getRailwayTicket().getDestination();
		Mockito.verify(account.getRailwayTicket()).getDestination();
		Assert.assertEquals("xiamen", account.getRailwayTicket().getDestination());
	}

	/**
	 * 4、模拟异常
	 * doThrow when
	 */
	@Test(expected = NullPointerException.class)
	public void do_throws() {
		Account account = Mockito.mock(Account.class);
		Mockito.doThrow(NullPointerException.class).when(account).getRailwayTicket();
		account.getRailwayTicket();
	}

	/**
	 * 5、使用注解模拟
	 * @Mock
	 */
	@Test
	public void annotation() {
		Assert.assertNotNull(mockedList);
	}

	/**
	 * 6、参数匹配
	 * 参数匹配器
	 */
	@Test
	public void with_args() {
		Comparable mock = Mockito.mock(Comparable.class);
		Mockito.when(mock.compareTo("a")).thenReturn(1);
		Mockito.when(mock.compareTo("b")).thenReturn(2);
		System.out.println(mock.compareTo("c"));
	}

	@Test
	public void with_unspecified_args() {
		List list = Mockito.mock(List.class);
		Mockito.when(list.get(Mockito.anyInt())).thenReturn(1);
		Mockito.when(list.contains(Mockito.argThat(new ArgumentMatcher<Integer>() {

			@Override
			public boolean matches(Integer argument) {
				// TODO Auto-generated method stub
				return argument == 1 || argument == 2;
			}

		}))).thenReturn(true);

		Assert.assertEquals(1, list.get(0));
		Assert.assertEquals(1, list.get(1));
		Assert.assertTrue(list.contains(1));
		Assert.assertTrue(list.contains(2));
		Assert.assertFalse(list.contains(3));
	}

	/**
	 * 7、捕获参数来进一步断言
	 * 参数捕获器
	 */
	@Test
	public void capture_args() {
		PersonDao personDao = Mockito.mock(PersonDao.class);
		PersonService personService = new PersonService(personDao);
		ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
		personService.update(1, "wangsf");
		Mockito.verify(personDao).update(argument.capture());
		argument.capture();
		Assert.assertEquals(1, argument.getValue().getId());
		Assert.assertEquals("wangsf", argument.getValue().getName());
	}

	/**
	 * 8、使用方法预期回调接口生成期望值
	 * Answer结构
	 */
	@Test
	public void answer_with_callback() {
		Mockito.when(mockedList.get(Mockito.anyInt())).thenAnswer(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				return "hello world:" + invocation.getArgument(0);
			}
		});
		Assert.assertEquals("hello world:1", mockedList.get(1));
		Assert.assertEquals("hello world:2", mockedList.get(2));
		Assert.assertEquals("hello world:3", mockedList.get(3));
	}

	/**
	 * 9、使用spy监控真实对象
	 */
	@Test
	public void spy() {
		List list = new ArrayList<>();
		List spyList = Mockito.spy(list);
		Mockito.doReturn("1").when(spyList).get(1);

		spyList.get(1);
		Mockito.verify(spyList).get(1);
		Assert.assertEquals("1", spyList.get(1));
	}

	/**
	 * 10、重置
	 */
	@Test
	public void reset() {
		Mockito.when(mockedList.size()).thenReturn(1);
		mockedList.size();
		Mockito.verify(mockedList).size();
		Assert.assertEquals(1, mockedList.size());
		Mockito.reset(mockedList);
		System.out.println(mockedList.size());
	}

	/**
	 * 11、验证调用次数
	 */
	@Test
	public void verifying_number_of_invocations() {

		mockedList.add(1);
		mockedList.add(1);
		mockedList.add(1);

		Mockito.verify(mockedList, Mockito.atLeast(1)).add(1);
		Mockito.verify(mockedList, Mockito.atMost(3)).add(1);
	}

	/**
	 * 12、验证调用顺序
	 */
	@Test
	public void verification_in_order() {
		List list1 = Mockito.mock(List.class);
		List list2 = Mockito.mock(List.class);
		list1.add(1);
		list1.add(2);
		list2.add(2);
		InOrder inOrder = Mockito.inOrder(list1);
		inOrder.verify(list1).add(1);
		inOrder.verify(list1).add(2);
	}

	/**
	 * 13、验证无交互
	 */
	@Test
	public void verify_interaction() {
		List list1 = Mockito.mock(List.class);
		List list2 = Mockito.mock(List.class);
		list1.add(1);
		//		list1.add(2);
		//		list2.add(2);
		Mockito.verify(list1).add(1);
		Mockito.verifyZeroInteractions(list1, list2);
	}

	class Person {
		private int id;
		private String name;

		Person(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	interface PersonDao {
		public void update(Person person);
	}

	class PersonService {
		private PersonDao personDao;

		PersonService(PersonDao personDao) {
			this.personDao = personDao;
		}

		public void update(int id, String name) {
			personDao.update(new Person(id, name));
		}
	}

	public class RailwayTicket {
		private String destination;

		public String getDestination() {
			return destination;
		}

		public void setDestination(String destination) {
			this.destination = destination;
		}
	}

	public class Account {
		private RailwayTicket railwayTicket;

		public RailwayTicket getRailwayTicket() {
			return railwayTicket;
		}

		public void setRailwayTicket(RailwayTicket railwayTicket) {
			this.railwayTicket = railwayTicket;
		}
	}
}
