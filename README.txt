前端模块 client-module port:9099
    -前端页面链接 http://localhost:9099/menu.jsp


后台模块 demomodule port:9090
    -1上传文件接口  http://localhost:9099/home/upload
    -2下载文件接口  http://localhost:9090/downloadFile
    -3获取文件元数据接口 http://localhost:9090/showDetail
    -4接口权限校验 http://localhost:9090/*
    -5文件查询接口 http://localhost:9090/showFile
