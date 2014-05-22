package net.techreadiness.rules;

import java.util.List;
import java.util.Map;

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
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TestStartsWithMapOperator {
	private static KnowledgeBase knowlegeBase;

	@BeforeClass
	public static void beforeClass() {
		KnowledgeBuilderConfiguration config = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
		config.setOption(EvaluatorOption.get("startsWith", new StartsWithEvaluatorDefinition()));

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(config);
		kbuilder.add(ResourceFactory.newClassPathResource("net/techreadiness/rules/starts-with-map.drl"), ResourceType.DRL);

		if (kbuilder.hasErrors()) {
			kbuilder.getErrors();
			throw new ServiceException("Problem building cross field validation rules.");
		}

		knowlegeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowlegeBase.addKnowledgePackages(kbuilder.getKnowledgePackages());
	}

	@Test
	public void startsWithPositive() {
		StatefulKnowledgeSession session = knowlegeBase.newStatefulKnowledgeSession();
		List<String> startedWith = Lists.newArrayList();
		session.setGlobal("startedWith", startedWith);
		List<Map<String, String>> toTest = Lists.newArrayList();
		Map<String, String> helloName = Maps.newHashMap();
		helloName.put("code", "hello name");
		toTest.add(helloName);

		for (Map<String, String> map : toTest) {
			session.insert(map);
		}

		session.fireAllRules();
		session.dispose();

		for (Map<String, String> map : toTest) {
			Assert.assertTrue(map.get("code") + ", starts with 'hello'.", startedWith.contains(map.get("code")));
		}
	}

	@Test
	public void startsWithNegative() {
		StatefulKnowledgeSession session = knowlegeBase.newStatefulKnowledgeSession();
		List<String> startedWith = Lists.newArrayList();
		session.setGlobal("startedWith", startedWith);
		List<Map<String, String>> toTest = Lists.newArrayList();
		Map<String, String> helloName = Maps.newHashMap();
		helloName.put("code", "helo name");
		toTest.add(helloName);

		for (Map<String, String> map : toTest) {
			session.insert(map);
		}

		session.fireAllRules();
		session.dispose();

		for (Map<String, String> map : toTest) {
			Assert.assertFalse(map.get("code") + ", does not start with 'hello'.", startedWith.contains(map.get("code")));
		}
	}
}
