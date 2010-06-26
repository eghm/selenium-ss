<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>Screenshots for test ${name}</title>

<style type="text/css">
html, body {
  font-family: Lucida Grande, Tahoma;  
  font-size: 8pt;  
}

.dropshadow {
  float:left;
  background: url(../css/shadowAlpha.png) no-repeat bottom right !important;
  background: url(../css/shadow.gif) no-repeat bottom right;
  margin: 10px 0 0 10px !important;
  margin: 10px 0 0 5px;
}

.dropshadow img {
  display: block;
  position: relative;
  background-color: #fff;
  border: 1px solid #a9a9a9;
  margin: -6px 6px 6px -6px;
  padding: 4px;  
}

.screenshot {
  float: left;
  margin: 10px;
}

h4 {
  border-bottom: 1px solid #A9A9A9;
  margin-bottom: 0;
  font-size: 10pt;
}

p {  
  margin-top: 0;  
}
</style>
</head>

<body>
<h4>Screenshots for test ${name}</h4>
<p>${screenshots?size} images. Test started at ${start}</p>

<#list screenshots as screenshot>

<div class="screenshot">
<a class="dropshadow" href="${screenshot.image}" title="${screenshot.description}">
    <img src ="${screenshot.image}" width="150"/>
<a>
</div>

</#list>    
</body>
</html>