package com.group6boun451.learner;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muaz on 07.10.2016.
 */
public class TopicContainer {
    private List<Topic> topics;
    private Context _context;
    public TopicContainer(Context context){
        this._context = context;
        topics = new ArrayList<Topic>();
        createTopics();
    }

    public List<Topic> getTopics(){
        return topics;
    }

    public Topic getTopic(int id){
        return this.topics.get(id);
    }
    private void createTopics(){
        Topic topic1 = new Topic(0);
        topic1.setText("Recent data shows that the population of the popular sushi fish has declined by roughly 97% from its historic levels due to overfishing. Scientific assessments by the International Scientific Committee for Tuna and Tuna-like species in the North Pacific Ocean found the current “unfished” population to be at 2.6%, a drastic decrease from the already low 4.2% in a previous estimate.\n" +
                "\n" +
                "Even with help and management of fisheries, the chances of getting the population back to a healthy level is only 0.1%. According to Time, reducing fishing by a fifth would increase that likelihood to 3%.");
        topic1.setTitle("Why is Lufer Population Decreasing");
        topic1.setEditor("Muaz Ekici");
        topic1.setDate("12.01.2016");
        topic1.setImage(R.drawable.lufertopic);
        topics.add(topic1);
        Topic topic2 = new Topic(1);
        topic2.setText("So, you’ve decided you want to start programming — you’re excited and ready to learn, and then you ask yourself, “Where do I begin, and which language do I start with?” The answer to that question is a little tough, and it’s certainly something I struggled with when I was starting out. That’s why today I want to dig into straightforward, useful advice you can work off immediately. And by the end of this, you’ll be wiser, braver, and hopefully a few steps closer to some cold, hard programming.");
        topic2.setTitle("Where to start coding");
        topic2.setEditor("Ahmet Zorer");
        topic2.setDate("18.04.2015");
        topic2.setImage(R.drawable.topiccode);
        topics.add(topic2);
        Topic topic3 = new Topic(2);
        topic3.setText("Learning how to choose a puppy that’s healthy takes effort and patience. Once you’ve found the right one, how do you know he’s ready to come home with you?\n" +
                "Timing can be everything. Ideally, the age to bring home a puppy is when he’s between seven and twelve weeks old. This is considered the critical socialization period in a puppy’s life, the time when he learns the most about his environment and begins to adjust to his surroundings.\n" +
                "If you find a puppy younger than six weeks, he may develop issues with separation anxiety and poor socialization habits. The puppy needs the first few weeks with his mother and other puppies from the litter to properly socialize. It will take a lot of hard work on the part of the owner to make sure a puppy so young grows up to be well adjusted. It’s not impossible, but it involves a lot of time and expertise and such a puppy may not be right for you.");
        topic3.setTitle("What Is The Best Age To Adopt a Puppy");
        topic3.setEditor("Esra Alınca");
        topic3.setDate("08.07.2016");
        topic3.setImage(R.drawable.topicpuppy);
        topics.add(topic3);
    }


}
