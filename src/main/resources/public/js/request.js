$ (document).ready(function () {
	var currencies={};
	$ .ajax({
		url: "http://localhost:9015/index/EuroRate",
		type: "POST",
		dataType: "text",
		success: function(data){
			console.log(JSON.parse(data));
			currencies = JSON.parse(data);
			console.log(currencies.CP);
			for (var i in currencies.CP){
				console.log(i+" "+currencies.CP[i])
				$('#CP').append("<div class='rate__couple'><span class='rate__currency'>"+i+"</span><span class='rate__value'>"+currencies.CP[i]+"<span></div>")
			}
			for (var i in currencies.Oxr){
				console.log(i+" "+currencies.Oxr[i])
				$('#Oxr').append("<div class='rate__couple'><span class='rate__currency'>"+i+"</span><span class='rate__value'>"+currencies.Oxr[i]+"<span></div>")
			}
			for (var i in currencies.Fr){
				console.log(i+" "+currencies.Fr[i])
				$('#Fr').append("<div class='rate__couple'><span class='rate__currency'>"+i+"</span><span class='rate__value'>"+currencies.Fr[i]+"<span></div>")
			}
		}
	});
})
