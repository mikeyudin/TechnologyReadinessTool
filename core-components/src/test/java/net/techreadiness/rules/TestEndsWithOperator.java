package net.techreadiness.rules;

import java.util.List;

import net.techreadiness.service.exception.ServiceException;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.builder.conf.EvaluatorOption;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TestEndsWithOperator {
	private static KnowledgeBase knowlegeBase;
	private StatefulKnowledgeSession session;
	private List<String> toTest;
	private List<String> endedWith;
	private List<String> endedWithNull;
	private List<String> nullEndedWith;
	private List<String> notEndsWith;

	@BeforeClass
	public static void beforeClass() {
		KnowledgeBuilderConfiguration config = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
		config.setOption(EvaluatorOption.get("endsWith", new EndsWithEvaluatorDefinition()));

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(config);
		kbuilder.add(ResourceFactory.newClassPathResource("net/techreadiness/rules/ends-with.drl"), ResourceType.DRL);

		if (kbuilder.hasErrors()) {
			throw new ServiceException("Problem building cross field validation rules: " + kbuilder.getErrors());
		}

		knowlegeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowlegeBase.addKnowledgePackages(kbuilder.getKnowledgePackages());

	}

	@Before
	public void before() {
		session = knowlegeBase.newStatefulKnowledgeSession();
		endedWith = Lists.newArrayList();
		session.setGlobal("endedWith", endedWith);
		endedWithNull = Lists.newArrayList();
		session.setGlobal("endedWithNull", endedWithNull);
		nullEndedWith = Lists.newArrayList();
		session.setGlobal("nullEndedWith", nullEndedWith);
		notEndsWith = Lists.newArrayList();
		session.setGlobal("notEndsWith", notEndsWith);
		toTest = Lists.newArrayList();
	}

	@After
	public void after() {
		session.dispose();
	}

	@Test
	public void startsWithPositive() {
		toTest.add("name hello");
		toTest.add("namehello");
		toTest.add(" hello");
		toTest.add("hello");
		for (String string : toTest) {
			session.insert(string);
		}

		session.fireAllRules();
		session.dispose();

		for (String string : toTest) {
			Assert.assertTrue(string + ", ends with 'hello'.", endedWith.contains(string));
		}
	}

	@Test
	public void startsWithNegative() {
		toTest.add("name helo");
		toTest.add("hello ");
		toTest.add("ello");
		toTest.add("namehelo");
		for (String string : toTest) {
			session.insert(string);
		}

		session.fireAllRules();
		session.dispose();

		for (String string : toTest) {
			Assert.assertFalse(string + ", does not end with 'hello'.", endedWith.contains(string));
		}
	}

	@Test
	public void endsWithNull() {
		toTest.add("helo name");
		toTest.add(" hello");
		toTest.add("null");
		toTest.add("heloname");
		for (String string : toTest) {
			session.insert(string);
		}

		session.fireAllRules();

		for (String string : toTest) {
			Assert.assertFalse(string + ", does not end with null.", endedWithNull.contains(string));
		}
	}

	@Test
	public void notEndsWith() {
		toTest.add("name helo");
		toTest.add(" helo");
		toTest.add("null");
		toTest.add("namehelo");
		for (String string : toTest) {
			session.insert(string);
		}

		session.fireAllRules();

		for (String string : toTest) {
			Assert.assertTrue(string + ", does not ends with 'hello'.", notEndsWith.contains(string));
		}
	}

	@Test
	public void nullEndsWith() {
		session.insert(null);

		session.fireAllRules();

		Assert.assertTrue("null, does not start with 'hello'.", nullEndedWith.isEmpty());
	}
}
