$(document).ready(function () {
	    $("#search-form").submit(function (event) {
	        event.preventDefault();
	        fire_ajax_submit();
	    });
	    
	    $("#slider-form").submit(function (event) {
	        event.preventDefault();
	        fire_ajax_submit();
	    });

	function fire_ajax_submit() {
		var fromTest = $("#from").val();
	    var toTest =  $("#to").val();
		
	    $.ajax({
	        type: "GET",
	        url: "/chart/search?from=" + fromTest + "&to=" + toTest,
	        success: function (data) {
	        	myChart.data = data;
	            myChart.update();
	        },
	        error: function (e) {
	        }
	    });
	}
})