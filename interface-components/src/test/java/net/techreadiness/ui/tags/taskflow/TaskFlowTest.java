package net.techreadiness.ui.tags.taskflow;

import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowState;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TaskFlowTest {
	private TaskFlowState taskFlow;
	private Task task1;
	private Task task2;
	private Task task3;
	private Task task4;

	@Before
	public void before() {
		taskFlow = new TaskFlowState();
		task1 = new Task("test1", "edit", "Test 1");
		task2 = new Task("test2", "save", "Test 2");
		task3 = new Task("test3", "action", "Test 3");
		task4 = new Task("test4", "edit", "Test 4");
	}

	@Test
	public void testNullList() {
		taskFlow.setTasks(null);

		Assert.assertFalse("A null list should not have a next element.", taskFlow.hasNext());
		Assert.assertFalse("A null list should not have a previous element.", taskFlow.hasPrevious());
		Assert.assertNull("A null list should not have a current element.", taskFlow.getCurrentTask());
	}

	@Test
	public void testEmptyList() {
		taskFlow.setTasks(Lists.<Task> newArrayList());

		Assert.assertFalse("An empty list should not have a next element.", taskFlow.hasNext());
		Assert.assertFalse("An empty list should not have a previous element.", taskFlow.hasPrevious());
		Assert.assertNull("An empty list should not have a current element.", taskFlow.getCurrentTask());
	}

	@Test
	public void testOneElementList() {
		taskFlow.setTasks(Lists.newArrayList(task1));

		Assert.assertFalse("A single element list should not have a next element.", taskFlow.hasNext());
		Assert.assertFalse("A single element list should not have a previous element.", taskFlow.hasPrevious());
		Assert.assertEquals("The current task should be the first element in the list.", task1, taskFlow.getCurrentTask());
	}

	@Test
	public void testTwoElementList() {
		taskFlow.setTasks(Lists.newArrayList(task1, task2));

		Assert.assertTrue("The list should have a next element.", taskFlow.hasNext());
		Assert.assertFalse("The list should not have a previous element.", taskFlow.hasPrevious());
		Assert.assertEquals("The current task should be the first element in the list.", task1, taskFlow.getCurrentTask());

		taskFlow.next();

		Assert.assertFalse("The list should not have a next element.", taskFlow.hasNext());
		Assert.assertTrue("The list should have a previous element.", taskFlow.hasPrevious());
		Assert.assertEquals("The current task should be the second element in the list.", task2, taskFlow.getCurrentTask());
	}

	@Test
	public void testSetCurrent() {
		taskFlow.setTasks(Lists.newArrayList(task1, task2));
		taskFlow.setCurrentTask(task2);
		Assert.assertFalse("The list should not have a next element.", taskFlow.hasNext());
		Assert.assertTrue("The list should have a previous element.", taskFlow.hasPrevious());
		Assert.assertEquals("The current task should be the second element in the list.", task2, taskFlow.getCurrentTask());

		taskFlow.previous();

		Assert.assertTrue("The list should have a next element.", taskFlow.hasNext());
		Assert.assertFalse("The list should not have a previous element.", taskFlow.hasPrevious());
		Assert.assertEquals("The current task should be the first element in the list.", task1, taskFlow.getCurrentTask());
	}

	@Test
	public void testThreeElementList() {
		taskFlow.setTasks(Lists.newArrayList(task1, task2, task3));
		taskFlow.next();

		Assert.assertTrue("The list should have a next element.", taskFlow.hasNext());
		Assert.assertTrue("The list should have a previous element.", taskFlow.hasPrevious());
		Assert.assertEquals("The current task should be the second element in the list.", task2, taskFlow.getCurrentTask());

		taskFlow.next();
		Assert.assertFalse("The list should not have a next element.", taskFlow.hasNext());
		Assert.assertTrue("The list should have a previous element.", taskFlow.hasPrevious());
		Assert.assertEquals("The current task should be the third element in the list.", task3, taskFlow.getCurrentTask());
	}

	@Test
	public void testSetCurrentNull() {
		taskFlow.setTasks(Lists.newArrayList(task1, task2, task3));
		taskFlow.setCurrentTask(null);
		Assert.assertEquals("The current task should be the first element in the list.", task1, taskFlow.getCurrentTask());
	}

	@Test
	public void testSetCurrentNotInList() {
		taskFlow.setTasks(Lists.newArrayList(task1, task2, task3));
		taskFlow.setCurrentTask(task4);
		Assert.assertEquals("The current task should be the first element in the list.", task1, taskFlow.getCurrentTask());
	}

	@Test
	public void testAddToList() {
		taskFlow.setTasks(Lists.<Task> newArrayList());
		taskFlow.getTasks().add(task1);

		Assert.assertFalse("A single element list should not have a next element.", taskFlow.hasNext());
		Assert.assertFalse("A single element list should not have a previous element.", taskFlow.hasPrevious());
		Assert.assertEquals("The current task should be the first element in the list.", task1, taskFlow.getCurrentTask());
	}

	@Test
	public void testAddTwoElementsToList() {
		taskFlow.setTasks(Lists.<Task> newArrayList());
		taskFlow.getTasks().add(task1);
		taskFlow.getTasks().add(task2);

		Assert.assertTrue("The list should have a next element.", taskFlow.hasNext());
		Assert.assertFalse("The list should not have a previous element.", taskFlow.hasPrevious());
		Assert.assertEquals("The current task should be the first element in the list.", task1, taskFlow.getCurrentTask());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddNullElementsToList() {
		taskFlow.setTasks(Lists.<Task> newArrayList(null, task1));

		Assert.assertFalse("The list should not have a next element.", taskFlow.hasNext());
		Assert.assertFalse("The list should not have a previous element.", taskFlow.hasPrevious());
		Assert.assertEquals("The current task should be the first element in the list.", task1, taskFlow.getCurrentTask());
	}
}
