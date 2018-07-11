<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>

<!--
温度id为c1, 湿度id为c2
card 的id为设备编码,如a1
card中img的id为设备编码_img,如a1_img
card中开关状态的id为设备编码_txt,如a1_txt

button中的id为设备编码_btn, 如d1_btn
-->

<html>

<head>
<meta charset="UTF-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no" />

<meta http-equiv="Expires" content="0">
<meta http-equiv="kiben" content="no-cache">

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"
	integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB"
	crossorigin="anonymous" />

<script type="text/javascript">
		var ser = '<%=request.getAttribute("serverIp")%>';
		//ser = "123.206.104.15"
</script>
<title></title>
</head>

<body>
	<nav
		class="navbar navbar-expand-sm bg-dark navbar-dark justify-content-between">
		<span class="navbar-text">家得福超市变电站监控系统</span> <span
			class="navbar-text">温度:<label id="c1">26</label>℃&nbsp;&nbsp;&nbsp;&nbsp;湿度:<label
			id="c2">50</label>%</span>
	</nav>
	<div class="container">
		<br />
		<div class="row">
			<div class="col-md-auto">
				<div class="nav flex-column nav-pills" id="v-pills-tab"
					role="tablist" aria-orientation="vertical">
					<a class="nav-link active" id="v-pills-dev-tab" data-toggle="pill"
						href="#v-pills-dev" role="tab" aria-controls="v-pills-dev"
						aria-selected="true">设备</a> <a class="nav-link"
						id="v-pills-video-tab" data-toggle="pill" href="#" role="tab">视频监控</a>
					<a class="nav-link" id="v-pills-alarm-tab"
						href="../eleInfo/ServletAlarm">报警记录</a> <a class="nav-link"
						id="v-pills-alarm-tab" href="../eleInfo/HistoryInfo">历史记录</a> <a
						class="nav-link" id="v-pills-settings-tab" data-toggle="pill"
						href="#v-pills-settings" role="tab"
						aria-controls="v-pills-settings">设置</a>
				</div>
			</div>
			<div class="col">
				<div class="tab-content" id="v-pills-tabContent">
					<div class="tab-pane fade show active" id="v-pills-dev"
						role="tabpanel" aria-labelledby="v-pills-dev-tab">
						<div class="row justify-content-center">

							<div class="col-6">
								<div id="a1" class="card" style="width: 180px">
									<img id="a1_img" class="card-img-top bg-info "
										src="img/door.png" style="height: 80px"></img>
									<div class="card-footer text-muted">
										<div class="row justify-content-center ">
											<div class="col-6">门&nbsp;&nbsp;禁</div>
											<div id="a1_txt" class="col-6">关</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-6">
								<div class="card " style="width: 180px">
									<img id="d1_img" class="card-img-top bg-success "
										src="img/fan.png" style="height: 80px"></img>
									<div class="card-footer text-muted">
										<div class="row justify-content-center ">
											<div class="col-6">风&nbsp;&nbsp;机</div>
											<div id="d1_txt" class="col-6">开</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<br />
						<div class="row justify-content-center">

							<div class="col-6">
								<div class="card" style="width: 180px">
									<img id="a2_img" class="card-img-top bg-danger "
										src="img/gas.png" style="height: 80px"></img>
									<div class="card-footer text-muted">
										<div class="row justify-content-center ">
											<div class="col-6">烟&nbsp;&nbsp;雾</div>
											<div id="a2_txt" class="col-6">关</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-6">
								<div class="card " style="width: 180px">
									<img id="d2_img" class="card-img-top bg-info "
										src="img/light.png" style="height: 80px"></img>
									<div class="card-footer text-muted">
										<div class="row justify-content-center ">
											<div class="col-6">照&nbsp;&nbsp;明</div>
											<div id="d2_txt" class="col-6">关</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<br />
						<div class="row justify-content-center">

							<div class="col-6">
								<div class="card" style="width: 180px">
									<img id="a3_img" class="card-img-top bg-info "
										src="img/water.png" style="height: 80px"></img>
									<div class="card-footer text-muted">
										<div class="row justify-content-center ">
											<div class="col-6">水&nbsp;&nbsp;浸</div>
											<div id="a3_txt" class="col-6">关</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-6">
								<div class="card " style="width: 180px">
									<img id="d3_img" class="card-img-top bg-info "
										src="img/air.png" style="height: 80px"></img>
									<div class="card-footer text-muted">
										<div class="row justify-content-center ">
											<div class="col-6">空&nbsp;&nbsp;调</div>
											<div id="d3_txt" class="col-6">关</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<br />
						<div class="row justify-content-center">
							<button id="d1_btn" type="button" class="col m-1 btn btn-info">风机启/停</button>
							<button id="d2_btn" type="button" class="col m-1 btn btn-info">照明开/关</button>
							<button id="d3_btn" type="button" class="col m-1 btn btn-info">空调开/关</button>
						</div>
					</div>
					<div class="tab-pane fade" id="v-pills-settings" role="tabpanel"
						aria-labelledby="v-pills-alarm-tab">
						<form>
							<div class="form-row">
								<div class="form-check col-md-6 offset-md-3">
									<input id="tem_linkage_enable" class="form-check-input"
										type="checkbox" value="" id="temLinkageEnable"> <label
										class="form-check-label" for="temLinkageEnable">
										温度连锁是否可用 </label>
								</div>
							</div>
							<br />
							<div class="input-group col-md-6 offset-md-3">
								<div class="input-group-append">
									<span class="input-group-text">当温度大于</span>
								</div>
								<input name="tem_great_value" id="tem_great_value" type="text"
									class="form-control"
									aria-label="Amount (to the nearest dollar)">
								<div class="input-group-append">
									<span class="input-group-text">℃ </span> <span
										class="input-group-text">打开风机</span>
								</div>
							</div>
							<br />
							<div class="input-group col-md-6 offset-md-3">
								<div class="input-group-append">
									<span class="input-group-text">当温度低于</span>
								</div>
								<input name="tem_less_value" id="tem_less_value" type="text"
									class="form-control"
									aria-label="Amount (to the nearest dollar)">
								<div class="input-group-append">
									<span class="input-group-text">℃ </span> <span
										class="input-group-text">关闭风机</span>
								</div>
							</div>
							<br />
							<div class="form-group row">
								<div class="col-md-3 offset-md-3">
									<button id="btn_submit" type="button" class="btn btn-primary">保存</button>
								</div>
							</div>
						</form>

					</div>
				</div>

			</div>
		</div>

	</div>
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js "
		integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo "
		crossorigin="anonymous "></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js "
		integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49 "
		crossorigin="anonymous "></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js "
		integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T "
		crossorigin="anonymous "></script>

	<script type="text/javascript" src="js/index.js"></script>
</body>

</html>