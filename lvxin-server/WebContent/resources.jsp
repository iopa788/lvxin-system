<%
	String resourceBasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<link rel="shortcut icon" href="<%=resourceBasePath%>/resource/img/favicon.ico"type="image/x-icon" />
<link rel="stylesheet" href="<%=resourceBasePath%>/resource/bootstrap-3.3.7-dist/css/bootstrap.min.css" />
<link rel="stylesheet" href="<%=resourceBasePath%>/resource/css/base-ui.css" />

<script type="text/javascript" src="<%=resourceBasePath%>/resource/js/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="<%=resourceBasePath%>/resource/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=resourceBasePath%>/resource/js/framework.js"></script>
<script type="text/javascript" src="<%=resourceBasePath%>/resource/js/jquery-ui.min.js"></script>
