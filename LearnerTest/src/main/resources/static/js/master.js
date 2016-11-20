// Put all your page JS here

var questions = [];

$(function () {
	
	console.log("Retrieving questions");
	
	
	$.getJSON('/topic/questions/' + topicID).done(function (data){
		if(data.length == 0){
			alert("No quiz questions is specified for this quiz! Returning to topic page");
			location.replace('/topic/'+topicID);
			
		}
		alert("Questions arrived!");
		console.log("Questions" + data);
		
		questions = $.map(data, function(dataQuestion){
			
			return {"q": dataQuestion.question, 
					"a":[{"option":dataQuestion.answerA ,"correct":dataQuestion.correct == 1},
						{"option":dataQuestion.answerB ,"correct":dataQuestion.correct == 2},
						{"option":dataQuestion.answerC ,"correct":dataQuestion.correct == 3}], 
					"correct" : " Correct answer! " + dataQuestion.explanation,
					"incorrect" : "Wrong answer! " + dataQuestion.explanation};
		});
		
		quizJSON.questions = questions;
		$('#slickQuiz').slickQuiz();
	}).fail(function(){
		console.log("Empty quiz!");
		alert("No quiz questions is specified for this quiz! Returning to topic page");
		location.replace('/topic/'+topicID);
	});
	
});
