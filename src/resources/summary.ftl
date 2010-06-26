<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>Test Summary</title>
<style type="text/css">
html, body {
  font-family: Lucida Grande, Tahoma;  
  font-size: 10pt;  
}
</style>
</head>

<body>

<ul>
<#list 1..tests as i>

<li><a href="test${i}/index.html">test${i}</a></li>

</#list>
</ul>
</body>
</html>