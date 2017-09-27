$(document).ready(function () {
	    $("#search-form").submit(function (event) {
	        event.preventDefault();
	        fire_ajax_submit();
	    });

	function fire_ajax_submit() {
		var fromTest = $("#from").val();
	    var toTest =  $("#to").val();
	    
	    if (fromTest == "" || toTest == "")
	    	return;
		
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

$(function () {
        $('#datetimepicker6').datetimepicker({
        	format: 'DD.MM.YYYY'
        });
        $('#datetimepicker7').datetimepicker({
            useCurrent: false, //Important! See issue #1075
            format: 'DD.MM.YYYY'
        });
        $("#datetimepicker6").on("dp.change", function (e) {
            $('#datetimepicker7').data("DateTimePicker").minDate(e.date);
        });
        $("#datetimepicker7").on("dp.change", function (e) {
            $('#datetimepicker6').data("DateTimePicker").maxDate(e.date);
        });
   });