package org.learner.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.learner.persistence.dao.CommentRepository;
import org.learner.persistence.dao.QuestionRepository;
import org.learner.persistence.dao.QuizResultRepository;
import org.learner.persistence.dao.TagRepository;
import org.learner.persistence.dao.TopicPackRepository;
import org.learner.persistence.dao.TopicRepository;
import org.learner.persistence.dao.UserRepository;
import org.learner.persistence.model.Comment;
import org.learner.persistence.model.Question;
import org.learner.persistence.model.QuizResult;
import org.learner.persistence.model.Tag;
import org.learner.persistence.model.Topic;
import org.learner.persistence.model.TopicPack;
import org.learner.persistence.model.User;
import org.learner.web.dto.TopicDto;
import org.learner.web.util.ConceptNetEdge;
import org.learner.web.util.ConceptNetModel;
import org.learner.web.util.ConceptNetSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

@Service
@Transactional
public class TopicService implements ITopicService{
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	TopicRepository repository;
	
	@Autowired 
	TagRepository tagRepo;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	TopicPackRepository packRepo;
	
	@Autowired
	QuestionRepository questionRepo;
	
	@Autowired
	QuizResultRepository quizResultRepo;
	@Override
	public Topic setTopicImage(long tid, String imgpath) {
		Topic edit = repository.findOne(tid);
		edit.setHeaderImage(imgpath);
		return edit;
	}

	@Override
	public Topic createNewTopic(TopicDto topicdto) {
		
		if((new Date()).before( topicdto.getRevealDate() ) ){
			//TODO reveal date exception
		}
		

		final Topic topic = new Topic();
		
		topic.setHeader(topicdto.getHeader());
		topic.setContent(topicdto.getContent());
		topic.setRevealDate(topicdto.getRevealDate());
		topic.setCreatedAt(new Date());
		
		final Authentication curAuth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = curAuth.getName();
        
        User owner = userRepo.findByEmail(currentUserName);
        topic.setOwner(owner);
		String topicPackName = topicdto.getTopicPackName();
        if(topicdto.getTopicPack() == null) {
        	if(topicPackName != null){
        		if(!topicPackName.equals("") && topicPackName.length() > 1){
        			TopicPack pack = new TopicPack();
        			pack.setName(topicPackName.substring(0, 1).toUpperCase() + topicPackName.substring(1).toLowerCase());
        			TopicPack dbpack =  packRepo.save(pack);
        			topic.setTopicPack(dbpack);
        		}
        	} else {
        		topic.setTopicPack(null);
        	}
        	
        } else {
        	TopicPack tp = packRepo.getOne(topicdto.getTopicPack());
        	
        	if(tp != null) {
        		topic.setTopicPack(tp);
        	}
        	
        }
        return repository.save(topic);
	}

	@Override
	public void deleteTopic(long id) {
		
	}

	@Override
	public Topic updateTopic(long id, TopicDto topicdto) {
		
		Topic edit = repository.findOne(id);
		if(edit == null){
			return null;
		}
		edit.setContent(topicdto.getContent());
		edit.setHeader(topicdto.getHeader());
		repository.save(edit);
		return edit;
	}

	@Override
	public Topic getTopicById(long id) {
		return repository.findOne(id);
	}

	@Override
	public Topic getTopicByHeader(String hdr) {
		
		return null;
	}

	@Override
	public Topic likeTopic(long topicId) {
		Topic liked = repository.findOne(topicId);
		if(liked == null){
			System.out.println("topic not found!");
			return null;
		}
		
		User user = getCurrentUser();
		System.out.println("Like User :" + user);
		
		if(!liked.getLikedBy().contains(user)){
			liked.getLikedBy().add(user);
			System.out.println("Like save!");
		} else {
			System.out.println("Already liked!");
			return null;
		}
		
		return liked;
	}
	
	@Override
	public Topic unlikeTopic(long topicId) {
		Topic liked = repository.findOne(topicId);
		if(liked == null){
			System.out.println("topic not found!");
			return null;
		}
		
		User user = getCurrentUser();
		System.out.println("User :" + user);
		
		liked.getLikedBy().remove(user);
		
		return liked;
	}
	
	
	@Override
	public List<Topic> getAllTopics() {
		return repository.findAll();
		
	}
	
	@Override
	public Comment createComment(long id, String content) {
		
		Comment cmnt = new Comment();
		cmnt.setContent(content);
		
		final Authentication curAuth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = curAuth.getName();
        
        User owner = userRepo.findByEmail(currentUserName);
        cmnt.setOwner(owner);
        
        Topic tpc = repository.findOne(id);
        cmnt.setRelatedTopic(tpc);
		
        cmnt.setCreatedAt(new Date());
		commentRepository.save(cmnt);
		return cmnt;
	}

	@Override
	public void deleteComment(long id) {
		commentRepository.delete(id);
	}

	@Override
	public List<Topic> getRecentTopics() {
		return repository.findTop5ByOrderByCreatedAtDesc();
	}

	@Override
	public List<Topic> getRecommendedTopics() {
		// TODO User specific recommendations
		List<Topic> allTopics = repository.findAll();
		List<Topic> likedTopics = new ArrayList<>();
		User current = getCurrentUser();
	
		for(Topic tp: allTopics){
			List<User> whoLiked = tp.getLikedBy();
			if(whoLiked.contains(current)){
				likedTopics.add(tp);
			}
		}
		
		List<Topic> recommendedTopics = new ArrayList<>();	
		
		for(Topic tpc: likedTopics){
			List<Topic> temp = getRelatedTopicsViaTopics(tpc);
			int counter=0;
			for(int i=0;i<temp.size();i++){
				if(!recommendedTopics.contains(temp.get(i)) && counter<2){
					recommendedTopics.add(temp.get(i));
					counter++;
				}
				if(counter==2){
					break;
				}
			}
		}
		
		recommendedTopics = recommendedTopics.subList(0,10);
		if(recommendedTopics.size()>10){
		}
		
		return recommendedTopics;
		
	}

	
	@Override
	public Tag createTagToTopic(Topic topic,Tag tag) {
		topic.getTags().add(tag);
		return tag;
	}


	@Override
	public List<Topic> basicKeywordSearch(String keyword) {
		
		return repository.findByHeaderContaining(keyword);
	}

	@Override
	public List<Topic> getRelatedTopicsViaTopics(Topic topic) {
		
		List<Tag> srcTopicTags = topic.getTags();
		List<Tag> alltags = tagRepo.findAll();
		
		
		List<Tag> nonzerotags = new ArrayList<Tag>();
		for(Tag tt : alltags){

			if(srcTopicTags.contains(tt)){
				tt.incrementSearchPoint(20);
			}
			if(tt.getConceptRelations().isEmpty()){
				//System.out.println("No concept for tag : " + tt.getName());
				continue;
			}
			
			for(Tag srcTag : srcTopicTags){
				Sets.SetView<String> sw =  Sets.intersection(srcTag.getConceptRelations(), tt.getConceptRelations());
				tt.incrementSearchPoint(sw.size() * 3 );
			}
			
			
			
			
			if(tt.getSearchPoint() > 0) {
				System.out.println("TAG : " + tt.getName());
				//System.out.println("Common : " + sw );
				//System.out.println("Size : " + sw.size());
				System.out.println("Search Points : " + tt.getSearchPoint());
				System.out.println("----");
				nonzerotags.add(tt);
			}
			
		}
		
		Collections.sort(nonzerotags, Comparator.comparingInt(Tag::getSearchPoint));
		
		
		List<Topic> weighted = new ArrayList<Topic>();
		System.out.println("Non-zero tag count : " + nonzerotags.size());
		
		for (Tag nztag : nonzerotags) {
			for(Topic wtop : nztag.getRelatedTopics()){
				int index = weighted.indexOf(wtop);
				if(index == -1){
					wtop.incrementSearchScore(nztag.getSearchPoint());
					weighted.add(wtop);
				} else {
					weighted.get(index).incrementSearchScore(nztag.getSearchPoint());
				}
			}
		}
		Collections.sort(weighted, Comparator.comparingInt(Topic::getSearchScore));
		return weighted;
	}

	@Override
	public List<Topic> getRelatedTopicsViaTags(long tagid) {
		Tag tag = tagRepo.findOne(tagid);
		if(tag == null) return null;
		return tag.getRelatedTopics();
	}

	@Override
	public List<Topic> getTopicsCreatedByUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Topic> getTopicsLikedByUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Topic> getTopicsCommentedByUser() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private User getCurrentUser(){
		final Authentication curAuth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = curAuth.getName();
        User owner = userRepo.findByEmail(currentUserName);
        return owner;
	}
	
	@Override
	public List<Tag> tagSuggest(String q) {
		
		return tagRepo.findByNameContaining(q);
	}

	@Override
	public Tag createTag(Tag tag) {
		tag.setCreatedAt(new Date());
		
		try{
			ConceptNetModel cnmodel = ConceptNetSearch.conceptNetQuery(tag.getName().toLowerCase() );
			HashSet<String> hs =  cnmodel.retrieveConcepts("/c/en/"+tag.getName().toLowerCase().replace(" ", "_"));
			tag.setConceptRelations(hs);
		} catch (Exception e) {
		System.out.println("Cannot create concepts");
			e.printStackTrace();
		}
		
		Tag created = tagRepo.save(tag);
		return created;
	}

	@Override
	public List<Tag> createTags(List<Tag> tags) {
		List<Tag> created = tagRepo.save(tags);
		return created;
	}

	@Override
	public List<Tag> createTagsToTopic(Topic topic, List<Tag> tags) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Topic> searchSuggest(String q) {
		List<Topic> topicSuggest = repository.findTop10ByHeaderContainingOrderByCreatedAtDesc(q);
		return topicSuggest;
	}

	@Override
	public Question createQuestions(Topic topic, TopicDto questions) {
		List<Question> ques = new ArrayList<>();
		for(int i = 0; i<questions.getQuestion().length ;i++){
			Question q = new Question();
			q.setQuestion(questions.getQuestion()[i]);
			q.setAnswerA(questions.getAnswerA()[i]);
			q.setAnswerB(questions.getAnswerB()[i]);
			q.setAnswerC(questions.getAnswerC()[i]);
			q.setCorrect(questions.getCorrect()[i]);
			q.setCorrect(questions.getCorrect()[i]);
			q.setExplanation(questions.getExplanation()[i]);
			q.setRelatedTopic(topic);
			ques.add(q);
		}
		if(ques.isEmpty()){
			return null;
		}
		questionRepo.save(ques);
		return ques.get(0);
	}

	@Override
	public List<Topic> getPopularTopics() {
		List<Topic> popular = repository.popularTopics(new PageRequest(0, 7));
		return popular;
	}
	
	@Override
	//TODO find topics with common tags
	public List<Topic> getTopicsWithCommonTag(Topic topic) {
		if(topic == null) return null;
		List<Tag> ttags = topic.getTags();
		//List<Long> tagids = ttags.stream().map(u -> u.getId()).collect(Collectors.toList());
		for(Tag t : ttags){
			System.out.println(t.getId() + " " + t.getName());
		}
	    

		if(ttags.isEmpty()) System.out.println("Empty!!!");
		
		List<Topic> popular = repository.topicsWithCommonTags(ttags,topic.getId());
		return popular;
	}

	@Override
	public boolean addQuestionsToTopic(Topic topic, List<Question> questions) {
		System.out.println("LOG -- Questions are added to topic");
		try {
			for(Question qt : questions){
				qt.setRelatedTopic(topic);
				questionRepo.save(qt);
			}
		} catch (Exception e) {
			System.out.println("LOG -- Failed adding questions.");
			return false;
		}
		
		return true;
	}

	@Override
	public boolean removeQuestion(Long questionId) {
		
		try {
			questionRepo.delete(questionId);
		} catch (Exception e) {
			System.out.println("Question deletion failed!");
			return false;
			
		}
		return true;
	}

	@Override
	public List<Topic> semanticSearch(String q) {
		q = q.toLowerCase();
		String searchterm = q.toLowerCase().replaceAll(" ", "_"); 
		ConceptNetModel cnmodel = ConceptNetSearch.conceptNetQuery(searchterm);
		
		List<ConceptNetEdge> edges = cnmodel.getEdges();
		
		Set<String> originalSet = new HashSet<String>();
		
		for(ConceptNetEdge cnedge: edges){
			int ei = cnedge.getEnd().lastIndexOf("/")+1;
			String end = cnedge.getEnd().substring(ei).replaceAll("_", " ");
			
			int si = cnedge.getStart().lastIndexOf("/")+1;
			String start = cnedge.getStart().substring(si).replaceAll("_", " ");
			
			int ri = cnedge.getRel().lastIndexOf("/")+1;
			String rel = cnedge.getRel().substring(ri).replaceAll("_", " ");
			
			System.out.println("Start : " + start + " , End : " + end + " ,Rel : " + rel + " Weight : " + cnedge.getWeigth());
			
			String related = end.equalsIgnoreCase(searchterm) ? start : end ;
			String originalform =  end.equalsIgnoreCase(searchterm) ? cnedge.getStart() : cnedge.getEnd();
			
			originalSet.add(originalform);
			
			System.out.println("Related : " + related);
			
		}
		
		List<Tag> alltags = tagRepo.findAll();
		
		List<Tag> nonzerotags = new ArrayList<Tag>();
		for(Tag tt : alltags){
			if(tt.getName().toLowerCase().startsWith(q)){
				tt.incrementSearchPoint(30);
			}
			
			if(tt.getConceptRelations().isEmpty()){
				//System.out.println("No concept for tag : " + tt.getName());
				continue;
			}
			
			if(tt.getConceptRelations().contains("/c/en/" + searchterm)){
				tt.incrementSearchPoint(15);
			}
			
			Sets.SetView<String> sw =  Sets.intersection(originalSet, tt.getConceptRelations());

			
			//TODO Give point sw.size * 3 ;
			tt.incrementSearchPoint(sw.size() * 5 );
			
			
			if(tt.getSearchPoint() > 0) {
				System.out.println("TAG : " + tt.getName());
				System.out.println("Common : " + sw );
				System.out.println("Size : " + sw.size());
				System.out.println("Search Points : " + tt.getSearchPoint());
				System.out.println("----");
				nonzerotags.add(tt);
			}
			
		}
		
		Collections.sort(nonzerotags, Comparator.comparingInt(Tag::getSearchPoint));
		
		List<Topic> nameSearchTopics = repository.findByHeaderContaining(q);

		
		
		List<Topic> weighted = new ArrayList<Topic>();
		System.out.println("Non-zero tag count : " + nonzerotags.size());
		
		for(Topic wtop : nameSearchTopics){
			int index = weighted.indexOf(wtop);
			if(index == -1){
				wtop.incrementSearchScore(45);
				weighted.add(wtop);
			} else {
				weighted.get(index).incrementSearchScore(45);
			}
		}
		
			
		
		for (Tag nztag : nonzerotags) {
			for(Topic wtop : nztag.getRelatedTopics()){
				int index = weighted.indexOf(wtop);
				if(index == -1){
					wtop.incrementSearchScore(nztag.getSearchPoint());
					weighted.add(wtop);
				} else {
					weighted.get(index).incrementSearchScore(nztag.getSearchPoint());
				}
			}
		}
		
		Collections.sort(weighted, Comparator.comparingInt(Topic::getSearchScore));
		return weighted;
	}
	
	@Override
	public List<Topic> latestTopicsFromFollowing(List<User> teachers){
		return repository.findByOwnerInOrderByCreatedAtDesc(teachers);
	}
	

	@Override
	public List<Topic> getOtherTopicsInPack(Topic topic){
		List<Topic> topicsInPack = new ArrayList<Topic>();
		TopicPack topicPack = topic.getTopicPack();
		if(topicPack !=null){
			List<Topic> tt = topicPack.getTopicList();
			if(tt != null) {
				topicsInPack.addAll(tt);
				topicsInPack.remove(topic);
			}
		}
		return topicsInPack;
	}
	
	@Override
	public List<Topic> getTopicsInPack(long topicPackId){
		TopicPack tpPack = packRepo.findOne(topicPackId);
		if(tpPack == null){
			return null;
		}
		
		List<Topic> topicsInPack = new ArrayList<Topic>();
		List<Topic> tt = tpPack.getTopicList();
		if(tt != null) {
			topicsInPack.addAll(tt);
		}
		return topicsInPack;
	} 

	@Override
	public QuizResult saveQuizResult(QuizResult quizResult) {
		User u = getCurrentUser();
		quizResult.setSolver(u);
		System.out.println("Quiz Correct : " + quizResult.getCorrect() );
		System.out.println("Quiz Total : " + quizResult.getQuestionCount() );
		System.out.println("Quiz Solver : " + quizResult.getSolver() );
		System.out.println("Quiz Title : " + quizResult.getTitle() );
		return quizResultRepo.save(quizResult);
	}

	@Override
	public List<TopicPack> packSuggest(String q) {
		
		return packRepo.findByNameContaining(q);
	}
	
	@Override
	public boolean deleteTagFromTopic(Topic top, Tag tag){
		System.out.println("Deleting tag from topic: ");
		boolean result  = top.getTags().remove(tag);
		return result;
	}
	
}
