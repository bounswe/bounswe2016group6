// Put all your page JS here

var questions = [];
var totalQuestions = 0;
var totalCorrect = 0;

$(function () {
	
	console.log("Retrieving questions");
	
	
	$.getJSON('/topic/questions/' + topicID).done(function (data){
		if(data.length == 0){
			alert("No quiz questions is specified for this quiz! Returning to topic page");
			location.replace('/topic/'+topicID);
			return;
			
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
	
	
	$("#resultSaveButton").click(function (){
		alert("Hello");
		alert("Total questions : " + totalQuestions  + " , Total correct: " + totalCorrect );
		var quizResult = {"title": "Topic Quiz", "correct" : totalCorrect , "questionCount": totalQuestions};
		$.ajax
	    ({
	        type: "POST",
	        url: '/quiz/' + topicID + '/result/save',
	        dataType: 'json',
	        contentType: 'application/json',
	        data: JSON.stringify(quizResult),
	        cache: false,
	        success: function () {

	        alert("Your quiz results are saved!"); 
	        }
	    })
	});
});
