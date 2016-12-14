package org.learner.persistence.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "topic")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Topic {
	@JsonView(JViews.Minimal.class)
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
	@JsonView(JViews.Minimal.class)
    @NotNull
    private String header;
    
    @Column(length = 10000)
    private String content;
    
    @JsonView(JViews.Minimal.class)
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User owner;
    
    private int likes;
    
    @ManyToMany
    @JoinTable(name = "topic_like",
    			joinColumns = {@JoinColumn(name="topic_id")},
    			inverseJoinColumns = {@JoinColumn(name = "user_id")}
    			)		
    private List<User> likedBy;
    
    @ManyToMany
    @JoinTable(	name = "topic_tag" , 
    			joinColumns = {@JoinColumn(name="topic_id")}, 
    			inverseJoinColumns={@JoinColumn(name="tag_id")}
    			)
    private List<Tag> tags;
    
    @OneToMany(mappedBy = "relatedTopic")
    @OrderBy("createdAt")
    private List<Comment> comments;
    
    @OneToMany(mappedBy="relatedTopic")
    private List<Question> questions;
    
    //@OneToMany(mappedBy = "masterTopic")
    //private List
    
    private String headerImage;
    private Date createdAt;
    private Date revealDate;
    


    @ManyToOne
    @JoinColumn(name="pack_id")
    private TopicPack topicPack;
    
    @Transient
    private int searchScore;
    
    
    public TopicPack getTopicPack() {
		return topicPack;
	}

	public void setTopicPack(TopicPack topicPack) {
		this.topicPack = topicPack;
	}
    
    public void incrementSearchScore(int inc){
    	this.searchScore += inc;
    }
    
    public int getSearchScore() {
		return searchScore;
	}


	public void setSearchScore(int searchScore) {
		this.searchScore = searchScore;
	}


	public Topic() {
        super();
    }
	
	
    public List<Question> getQuestions() {
		return questions;
	}


	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}


	public List<User> getLikedBy() {
		return likedBy;
	}


	public void setLikedBy(List<User> likedBy) {
		this.likedBy = likedBy;
	}


	public List<Tag> getTags() {
		return tags;
	}


	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}


	public List<Comment> getComments() {
		return comments;
	}


	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}


	public String getHeaderImage() {
		return headerImage;
	}


	public void setHeaderImage(String headerImage) {
		this.headerImage = headerImage;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getRevealDate() {
		return revealDate;
	}

	public void setRevealDate(Date revealDate) {
		this.revealDate = revealDate;
	}




    public int getLikes() {
		return likes;
	}


	public void setLikes(int likes) {
		this.likes = likes;
	}


	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((header == null) ? 0 : header.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Topic other = (Topic) obj;
        
        if(!header.equals(other.header)){
        	return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Topic :").append(header);
        return builder.toString();
    }

}
