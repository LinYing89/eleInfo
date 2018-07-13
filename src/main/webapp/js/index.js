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
//A相电流
var li_axA;
//B相电流
var li_bxA;
//C相电流
var li_cxA;
//A相电压
var li_axV;
//B相电压
var li_bxV;
//C相电压
var li_cxV;
//功率因数
var li_yinshu;
var li_axyg;
var li_axwg;
var li_bxyg;
var li_bxwg;
var li_zyg;
var li_zwg;

var tem_great_value;
var tem_less_value;

var websocket = null;
var heart;
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
	
	li_axA = document.getElementById("axA");
	//alert(li_axA);
	//li_axA.innerText="3";
	//li_axA.innerText=123;
	li_bxA = document.getElementById("bxA");
	li_cxA = document.getElementById("cxA");
	li_axV = document.getElementById("axV");
	li_bxV = document.getElementById("bxV");
	li_cxV = document.getElementById("cxV");
	li_yinshu = document.getElementById("yinshu");
	li_axyg = document.getElementById("axyg");
	li_axwg = document.getElementById("axwg");
	li_bxyg = document.getElementById("bxyg");
	li_bxwg = document.getElementById("bxwg");
	li_zyg = document.getElementById("zyg");
	li_zwg = document.getElementById("zwg");

	tem_great_value = document.getElementById("tem_great_value");
	tem_less_value = document.getElementById("tem_less_value");

	var btnSubmit = document.getElementById("btn_submit");

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
	
	initWebSocket();
	
	//alert("test1");
	//test1();
}

function ctrlClick(which) {

	switch (which) {
	case 0:
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

// 格式 {"coding":"a1", "value":1.0}
function analysis(message) {
	//alert(message);
	//心跳
	if(message == "H"){
		return;
	}
	
	var obj = JSON.parse(message);
	var value = obj.value;
	if (obj.id == 0) {
		switch (obj.coding) {
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
			case "ele":
			li_axA.innerText = obj.axA;
			li_bxA.innerText = obj.bxA;
			li_cxA.innerText = obj.cxA;
			li_axV.innerText = obj.axV;
			li_bxV.innerText = obj.bxV;
			li_cxV.innerText = obj.cxV;
			li_yinshu.innerText = obj.yinshu;
			li_axyg.innerText = obj.axyg;
			li_axwg.innerText = obj.axwg;
			li_bxyg.innerText = obj.bxyg;
			li_bxwg.innerText = obj.bxwg;
			li_zyg.innerText = obj.zyg;
			li_zwg.innerText = obj.zwg;
			break;
		}
	} else if (obj.id == 1) {
		if (obj.symbol == "GREAT") {
			tem_great_value.value = value;
		} else if (obj.symbol == "LESS") {
			tem_less_value.value = value;
		}
	} else if (obj.id == 2) {
		alert("保存成功");
	}
}

// 0报警,1不报警
function alarm(img, txt, value) {
	if (value == 0) {
		img.setAttribute("class", "card-img-top bg-danger");
		txt.innerText = "异常";
	} else {
		img.setAttribute("class", "card-img-top bg-info");
		txt.innerText = "正常";
	}
}

// 0开,1关
function device(img, txt, value, btn) {
	if (value == 0) {
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

function initWebSocket() {
	// 变量ser在index.jsp文件中初始化,读取request的参数需要在jsp文件中
	if ('WebSocket' in window) {
		ser="localhost";
		websocket = new WebSocket("ws://" + ser + "/eleInfo/websocket");
	} else {
		alert("浏览器不支持websocket");
	}

	websocket.onerror = function() {

	};

	websocket.onopen = function() {
		send("rf");
		//开启定时心跳
		heart = self.setInterval("sendHeart()",5000);
	};

	websocket.onmessage = function(event) {
		analysis(event.data);
	};

	websocket.onclose = function() {
		//关闭定时心跳
		if(heart != null){
			self.clearInterval(heart)
		}
	};

}

function sendHeart(){
	send("H");
}

function clearTime() {
	closeWebSocket();
}

function closeWebSocket() {
	if(websocket != null){
		websocket.close();
	}
}

function send(message) {
	if (null != websocket) {
		websocket.send(message);
	}
}

window.onunload = clearTime;

function test1() {
	var t1 = '{"id":0,"coding":"axA","value":1}'
	alert(t1);
	analysis(t1);
	//	
	var t2 = '{"id":0,"coding":"bxA","value":2}'
	analysis(t2);

	var t3 = '{"id":0,"coding":"cxA","value":3}'
	analysis(t3);
	
	t1 = '{"id":0,"coding":"axV","value":4}'
	analysis(t1);
	//	
	t2 = '{"id":0,"coding":"bxV","value":5}'
	analysis(t2);

	t3 = '{"id":0,"coding":"cxV","value":6}'
	analysis(t3);
	
	t3 = '{"id":0,"coding":"yinshu","value":7}'
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