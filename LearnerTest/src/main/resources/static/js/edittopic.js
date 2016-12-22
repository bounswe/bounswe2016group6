var topicID;
var topicPackId;
var topicPackName;

$(document).ready(function() {
    initEditorContent();
    $('.nav-tabs li:first-child').addClass('active');
    $("div.questionContainer .addQuestion").click(function() {
        $(".topicQuestion").clone(true).insertAfter("div.topicQuestion:last").find("input[type='text']").val("");
    });
    $("div.topicQuestion .rmQuestion").click(function() {
        if ($('.topicQuestion').length > 1) {
            $(this).closest('.topicQuestion').remove();
        } else {
            alert("Cannot remove first question!");
        }
    });
    
    
    ////////////
    $('#summernote').summernote({
        toolbar: [
            ['style', ['style']],
            ['font', ['bold', 'italic', 'underline', 'clear']],
            ['insert', ['link', 'picture', 'video', 'hr']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['view', ['codeview']]
        ]
    });
    $('#summernote').summernote({
        height: 200,
        onImageUpload: function(files, editor, welEditable) {
            sendFile(files[0], editor, welEditable);
        }
    });

    function sendFile(file, editor, welEditable) {
        data = new FormData();
        data.append("file", file);
        $.ajax({
            data: data,
            type: "POST",
            url: "/topic/imageUpload",
            cache: false,
            contentType: false,
            processData: false,
            success: function(url) {
                editor.insertImage(welEditable, url);
            }
        });
    }
    $(':file').change(function() {
        var file = this.files[0];
        var name = file.name;
        var size = file.size;
        var type = file.type;
        alert("Name :" + name + " Size: " + type + " Type: " + type);
    });

    function progressHandlingFunction(e) {
        if (e.lengthComputable) {
            $('progress').attr({
                value: e.loaded,
                max: e.total
            });
        }
    }

    /////////////////////
/*
    $.getJSON('/topic/questions/' + topicID).done(function(data) {
        if (data.length == 0) {
            alert("No quiz questions is specified for this quiz! Returning to topic page");
            //location.replace('/topic/' + topicID);
            return;

        }
        alert("Questions arrived!");
        console.log("Questions" + data);

        questions = $.map(data, function(dataQuestion) {

            return {
                "q": dataQuestion.question,
                "a": [{
                    "option": dataQuestion.answerA,
                    "correct": dataQuestion.correct == 1
                }, {
                    "option": dataQuestion.answerB,
                    "correct": dataQuestion.correct == 2
                }, {
                    "option": dataQuestion.answerC,
                    "correct": dataQuestion.correct == 3
                }],
                "correct": " Correct answer! " + dataQuestion.explanation,
                "incorrect": "Wrong answer! " + dataQuestion.explanation
            };
        });

    }).fail(function() {
        console.log("Empty quiz!");
        alert("No quiz questions is specified for this quiz! Returning to topic page");
        location.replace('/topic/' + topicID);
    });
*/
    ////////////////////////////////////////////

    $('#tagfi').tagsinput({
        itemValue: 'name',
        itemText: 'name'
    });



    $('#tagfi').on('beforeItemRemove', function(event) {
        var tag = event.item;
        $.get("/" + topicID + "/remove/" + tag.id, function() {

            })
            .done(function(data) {
                if (data.error != null) {
                	console.log("Cannot remove tag!");
                }
            })
        console.log("Remove");
    });

    $.getJSON( '/topic/' + topicID + '/tags', function() {
    	  console.log( "Retrieve tags and fill...." );
    	})
    	  .done(function(data) {
    		  console.log(data);
    		  $.each( data, function( key, val ) {
    			  $('#tagfi').tagsinput('add', val);
    			 }); 
    	    console.log( "tag return" );
    	  })
    	  .fail(function() {
    	    console.log( "error" );
    	  })
    
  
    /////////////////////////
    $('#autocompleteTag').autocomplete({
        serviceUrl: '/tag/suggest',
        deferRequestBy: 300,
        minChars: 3,
        onSelect: function(suggestion) {
            //alert('You selected: ' + suggestion.value + ', ' + suggestion.data + ', ' + suggestion.context);
            $('#tagfi').tagsinput('add', {
                "name": suggestion.name,
                "id": suggestion.id,
                "context": suggestion.context
            });
            $('#autocompleteTag').autocomplete("clear");
        },
        paramName: 'query',
        transformResult: function(response) {
            return {
                suggestions: $.map($.parseJSON(response), function(dataItem) {
                    return {
                        value: dataItem.name + " - ( " + dataItem.context + " )",
                        data: dataItem.id,
                        id: dataItem.id,
                        name: dataItem.name,
                        context: dataItem.context
                    };
                })
            };
        }
    });
    //Topic Pack autocomplete packInputField
    $('#packInputField').autocomplete({
        serviceUrl: '/topic/pack/suggest',
        paramName: 'q',
        deferRequestBy: 200,
        minChars: 2,
        onSelect: function(suggestion) {
            //alert('You selected: ' + suggestion.value + ', ' + suggestion.data);
            $("#tpPackName").val(suggestion.data);
        },
        transformResult: function(response) {
            return {
                suggestions: $.map($.parseJSON(response), function(dataItem) {
                    return {
                        value: dataItem.name,
                        data: dataItem.id,
                    };
                })
            };
        }
    });
    //TOPIC CREATION
    $('#topicform').submit(function() {
        //alert("Form submit!");
        event.preventDefault();
        var markupStr = $('#summernote').summernote('code');
        console.log(markupStr);
        $("#tpContent").val(markupStr);
        var formData = new FormData($('#topicform')[0]);
        console.log(formData);
        var toprec = $.ajax({
            url: '/topic/create', //Server script to process data
            type: 'POST',
            xhr: function() { // Custom XMLHttpRequest
                var myXhr = $.ajaxSettings.xhr();
                if (myXhr.upload) { // Check if upload property exists
                    myXhr.upload.addEventListener('progress', progressHandlingFunction, false); // For handling the progress of the upload
                }
                return myXhr;
            },
            //Ajax events
            beforeSend: function() {
               // alert("Sending!");
            },
            // Form data
            data: formData,
            //Options to tell jQuery not to process data or worry about content-type.
            cache: false,
            contentType: false,
            processData: false
        });
        toprec.done(function(data) {
            //alert("Sending complete!");
            var topicID = data.message;
            //location.replace('/topic/'+data.message);
            $.ajax({
                url: '/tag/' + topicID + '/add',
                type: 'POST',
                data: JSON.stringify($('#tagfi').tagsinput('items')),
                contentType: 'application/json',
                dataType: 'json'
            }).done(function(response) {
                alert(response.message);
                location.replace('/topic/' + topicID);
            }).fail(function(response) {
                alert("Tag creation failed!");
            });
        }).fail(function(data) {
            alert("Error in topic creation!");
        });
        return true;
    });
});