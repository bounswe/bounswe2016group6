package com.group6boun451.learner;

import android.content.Context;

import com.group6boun451.learner.model.Comment;
import com.group6boun451.learner.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by muaz on 05.11.2016.
 */
public class CommentContainer {
    private List<Comment> comments;
    private Context _context;

    //constructor
    public CommentContainer(Context context){

        this._context = context;
        comments = new ArrayList<Comment>();
        createComments();
    }

    //get all comment in the container
    public List<Comment> getComments(){
        return comments;
    }

    // get a comment with an id
    public Comment getComment(int id ){
       return comments.get(id);
    }
    // creating comments now for only test purposes
    public void createComments(){
        Comment comment0 = new Comment(1);
        comment0.setContent("Global fisheries collapse in this report tells all about the ususal suspect \"those bad fishing guys\" Alas this simple minded 'bad guy' story doesn't tell the real story... 'Who Killed Jack Mackerel\" does... even better it describes how help is on the way!  http://russgeorge.net/2015/12/20/chilean-mackerel/");
        comment0.setCreatedAt(new Date(5,11,2016));
        comment0.setOwner(new User());
        comment0.getOwner().setFirstName("Ali Can");
        comment0.getOwner().setPicture("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcR8DP3unnpYATUtlJhxuadkcXbitC6eDXt9ANE_UP6vpmHBdP5dJbX8DQ");
        comments.add(comment0);
        Comment comment1 = new Comment(2);
        comment1.setContent("With human population now on target to be 11 billion by 2100, there is no way, in my opinion, to stop fish populations from vanishing. We are very good at preventing human death via modern medical technology, and very bad at knowing how to maintain the resulting hordes. It seems that most people in the U.S. want to assist starving people and even allow many to come here, without considering that at the present rate of increase, we are due to have up to 1.2 billion U.S. residents by 2100, and they are welcomed by many. Having traveled for years in Southeast Asia, the fishing fleets that I've seen seem to be in line with the massive over harvests that this article speaks of. Our technology is killing us, and we are voluntarily proceeding with it. I'd like folks to be able to see what India and China are like, but most simply say that technology will find a way. It will not.");
        comment1.setCreatedAt(new Date(6,11,2016));
        comment1.setOwner(new User());
        comment1.getOwner().setFirstName("Arda");
        comment1.getOwner().setPicture("https://scontent-fra3-1.xx.fbcdn.net/v/l/t1.0-1/p160x160/13413793_576986412470125_8959362392844844092_n.jpg?oh=c877109e8dd874ade70149a471f04d91&oe=58901645");
        comments.add(comment1);
        Comment comment2 = new Comment(3);
        comment2.setContent("In the 1980's, I worked for the Last Union Salmon Cannery in the Pacific Northwest. Our Union and its' members, associated fishermen, etc., were all telling the BIG OFFICE that not only were they depleting fish stocks but also phasing out our jobs, 20 years down the road. They didn't listen and we were right.");
        comment2.setCreatedAt(new Date(27,1,2016));
        comment2.setOwner(new User());
        comment2.getOwner().setFirstName("Erhan");
        comment2.getOwner().setPicture("https://scontent-fra3-1.xx.fbcdn.net/v/t1.0-1/c63.63.791.791/s160x160/68157_10151299599819390_853147536_n.jpg?oh=aeb97d6f7cb2e1d6efaad48012fae923&oe=58D36308");
        comments.add(comment2);
        Comment comment3 = new Comment(4);
        comment3.setContent("Dead Zones were once rare. The toxic so-called Pacific Blob is a new event. Will Blobs like Dead Zones, spread worldwide. Time will tell. But I think we can count on lots of Blobs worldwide in the near future.\n" +
                "Once they are common what real impact will these man-caused monsters have on everything else, especially the spiralling expansion of jelly fish? Will the devastation of marine life mean fresh water lakes like North America's Great Lakes will eventually be stocked with fast growing and uncontrollable Asian Carp and other edible invasive species so they can be harvested for food for an increasingly hungry world? You betcha.\n");
        comment3.setCreatedAt(new Date(27,5,2016));
        comment3.setOwner(new User());
        comment3.getOwner().setFirstName("Gufte Seren");
        comment3.getOwner().setPicture("https://scontent-fra3-1.xx.fbcdn.net/v/t1.0-1/c57.0.160.160/p160x160/12524108_1061274883893894_2201577191260070709_n.jpg?oh=066192855531272f38e79149db075e07&oe=58CF0D63");
        comments.add(comment3);
        Comment comment4 = new Comment(5);
        comment4.setContent("I grew up on Casco Bay, Portland, Me. Both sides of the tidal river had fish packing plants along the rivers edge. The cod were fished till the fishery collapsed. Then the menhaden were fished till collapse. Then the sardines were fished to collapse. Then the fish factories collapsed do lack of fish. The only fishery that was saved was the Maine lobster. Limits in size and the lobster with eggs had to be thrown back. Today there is still a strong fishery. Humans have to live within the limits of sustainability or we will kill ourselves.");
        comment4.setCreatedAt(new Date(22,8,2016));
        comment4.setOwner(new User());
        comment4.getOwner().setFirstName("Melih");
        comment4.getOwner().setPicture("https://scontent-fra3-1.xx.fbcdn.net/v/t1.0-0/p110x80/10734164_10152764899032398_2143712839067115392_n.jpg?oh=fca9f3e49b669ec529cd91aa7bec26d9&oe=58CE9288");
        comments.add(comment4);
    }


}
