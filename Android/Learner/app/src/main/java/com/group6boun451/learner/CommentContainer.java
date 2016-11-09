package com.group6boun451.learner;

import android.content.Context;

import java.util.ArrayList;
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
        comment0.setText("Global fisheries collapse in this report tells all about the ususal suspect \"those bad fishing guys\" Alas this simple minded 'bad guy' story doesn't tell the real story... 'Who Killed Jack Mackerel\" does... even better it describes how help is on the way!  http://russgeorge.net/2015/12/20/chilean-mackerel/");
        comment0.setDate("05.11.2016");
        comment0.setUserName("Ali Can Erkilic");
        comment0.setUsrImgId(R.drawable.usericon);
        comments.add(comment0);
        Comment comment1 = new Comment(2);
        comment1.setText("With human population now on target to be 11 billion by 2100, there is no way, in my opinion, to stop fish populations from vanishing. We are very good at preventing human death via modern medical technology, and very bad at knowing how to maintain the resulting hordes. It seems that most people in the U.S. want to assist starving people and even allow many to come here, without considering that at the present rate of increase, we are due to have up to 1.2 billion U.S. residents by 2100, and they are welcomed by many. Having traveled for years in Southeast Asia, the fishing fleets that I've seen seem to be in line with the massive over harvests that this article speaks of. Our technology is killing us, and we are voluntarily proceeding with it. I'd like folks to be able to see what India and China are like, but most simply say that technology will find a way. It will not.");
        comment1.setDate("05.01.2016");
        comment1.setUserName("Arda Akdemir");
        comment1.setUsrImgId(R.drawable.usericon);
        comments.add(comment1);
        Comment comment2 = new Comment(3);
        comment2.setText("In the 1980's, I worked for the Last Union Salmon Cannery in the Pacific Northwest. Our Union and its' members, associated fishermen, etc., were all telling the BIG OFFICE that not only were they depleting fish stocks but also phasing out our jobs, 20 years down the road. They didn't listen and we were right.");
        comment2.setDate("27.01.2016");
        comment2.setUserName("Erhan Cagirici");
        comment2.setUsrImgId(R.drawable.usericon);
        comments.add(comment2);
        Comment comment3 = new Comment(4);
        comment3.setText("Dead Zones were once rare. The toxic so-called Pacific Blob is a new event. Will Blobs like Dead Zones, spread worldwide. Time will tell. But I think we can count on lots of Blobs worldwide in the near future.\n" +
                "Once they are common what real impact will these man-caused monsters have on everything else, especially the spiralling expansion of jelly fish? Will the devastation of marine life mean fresh water lakes like North America's Great Lakes will eventually be stocked with fast growing and uncontrollable Asian Carp and other edible invasive species so they can be harvested for food for an increasingly hungry world? You betcha.\n");
        comment3.setDate("27.05.2016");
        comment3.setUserName("Gufte Seren Surmeli");
        comment3.setUsrImgId(R.drawable.usericon);
        comments.add(comment3);
        Comment comment4 = new Comment(5);
        comment4.setText("I grew up on Casco Bay, Portland, Me. Both sides of the tidal river had fish packing plants along the rivers edge. The cod were fished till the fishery collapsed. Then the menhaden were fished till collapse. Then the sardines were fished to collapse. Then the fish factories collapsed do lack of fish. The only fishery that was saved was the Maine lobster. Limits in size and the lobster with eggs had to be thrown back. Today there is still a strong fishery. Humans have to live within the limits of sustainability or we will kill ourselves.");
        comment4.setDate("22.08.2016");
        comment4.setUserName("Melih Demiroren");
        comment4.setUsrImgId(R.drawable.usericon);
        comments.add(comment4);
    }


}
