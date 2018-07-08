var a1_img;
var a1_txt;
var a2_img;
var a2_txt;
var a3_img;
var a3_txt;

var d1_img;
var d1_txt;
var d2_img;
var d2_txt;
var d3_img;
var d3_txt;

var c1;
var c2;

var d1_btn;
var d2_btn;
var d3_btn;

var tem_great_value;
var tem_less_value;
	
window.onload = prepare;

function prepare() {		
	a1_img = document.getElementById("a1_img");
	a1_txt = document.getElementById("a1_txt");
	a2_img = document.getElementById("a2_img");
	a2_txt = document.getElementById("a2_txt");
	a3_img = document.getElementById("a3_img");
	a3_txt = document.getElementById("a3_txt");
	d1_img = document.getElementById("d1_img");
	d1_txt = document.getElementById("d1_txt");
	d2_img = document.getElementById("d2_img");
	d2_txt = document.getElementById("d2_txt");
	d3_img = document.getElementById("d3_img");
	d3_txt = document.getElementById("d3_txt");
	c1 = document.getElementById("c1");
	c2 = document.getElementById("c2");

	d1_btn = document.getElementById("d1_btn");
	d2_btn = document.getElementById("d2_btn");
	d3_btn = document.getElementById("d3_btn");
	
	tem_great_value = document.getElementById("tem_great_value");
	tem_less_value = document.getElementById("tem_less_value");

	var btnSubmit = document.getElementById("btn_submit");
	
	d1_btn.onclick = function() {
		alert("function");
	}

	d1_btn.onclick = function() {
		ctrlClick(0);
	};
	d2_btn.onclick = function() {
		ctrlClick(1);
	};
	d3_btn.onclick = function() {
		ctrlClick(2);
	};
	btnSubmit.onclick = function() {
		var json = new Object();
		json.great = tem_great_value.value;
		json.less = tem_less_value.value;
		send(JSON.stringify(json));
	};
}

function ctrlClick(which) {

	switch(which) {
		case 0:
			//00 00 00 00 00 04 00 00 05 00 10 FF 00 ff ff
			var by = [00, 00, 00, 00, 00, 04, 00, 00, 05, 00, 10, 0xFF, 00, 0xff, 0xff];
			send("d1");
			break;
		case 1:
			send("d2");
			break;
		case 2:
			send("d3");
			break;
	}
}

//格式 {"coding":"a1", "value":1.0}
function analysis(message) {
	var obj = JSON.parse(message);
	var value = obj.value;
	if(obj.id == 0){
	switch(obj.coding) {
		case "a1":
			alarm(a1_img, a1_txt, value);
			break;
		case "a2":
			alarm(a2_img, a2_txt, value);
			break;
		case "a3":
			alarm(a3_img, a3_txt, value);
			break;
		case "d1":
			device(d1_img, d1_txt, value, d1_btn);
			break;
		case "d2":
			device(d2_img, d2_txt, value, d2_btn);
			break;
		case "d3":
			device(d3_img, d3_txt, value, d3_btn);
			break;
		case "c1":
			c1.innerText = value;
			break;
		case "c2":
			c2.innerText = value;
			break;
	}
	}else if(obj.id == 1){
		if(obj.symbol == "GREAT"){
			tem_great_value.value = value;
		}else if(obj.symbol == "LESS"){
			tem_less_value.value = value;
		}
	}else if(obj.id == 2){
		alert("保存成功");
	}
}

//0报警,1不报警
function alarm(img, txt, value) {
	if(value == 0) {
		img.setAttribute("class", "card-img-top bg-danger");
		txt.innerText = "异常";
	} else {
		img.setAttribute("class", "card-img-top bg-info");
		txt.innerText = "正常";
	}
}

//0开,1关
function device(img, txt, value, btn) {
	if(value == 0) {
		img.setAttribute("class", "card-img-top bg-success");
		txt.innerText = "开";
		btn.setAttribute("class", "col m-1 btn btn-success");
	} else {
		img.setAttribute("class", "card-img-top bg-info");
		txt.innerText = "关";
		btn.setAttribute("class", "col m-1 btn btn-info");
	}
}

function sendMessage() {

}

var websocket = null;
if('WebSocket' in window) {
	websocket = new WebSocket("ws://192.168.1.116/eleInfo/websocket");
} else {
	alert("浏览器不支持websocket");
}

websocket.onerror = function() {
	
};

websocket.onopen = function() {
	send("rf");
};

websocket.onmessage = function(event) {
	analysis(event.data);
};

websocket.onclose = function() {};

function clearTime() {
	closeWebSocket();
}

function closeWebSocket() {
	websocket.close();
}

function send(message) {
	websocket.send(message);
}

window.onunload = clearTime;

function test1() {
	var t1 = '{"coding":"a1","value":1.0}'
	analysis(t1);
	//	
	var t2 = '{"coding":"a2","value":0}'
	analysis(t2);

	var t3 = '{"coding":"d1","value":1}'
	analysis(t3);
}

function test2() {
	var t1 = '{"coding":"a1","value":0}'
	analysis(t1);
	//	
	var t2 = '{"coding":"a2","value":1}'
	analysis(t2);

	var t3 = '{"coding":"d1","value":0}'
	analysis(t3);
}