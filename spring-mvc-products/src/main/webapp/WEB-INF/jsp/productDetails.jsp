<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title><c:out value="${product.name}"/></title>
    <style>
        body {  }
        dl { display: grid; grid-template-columns: 8rem 1fr; gap: .3rem 1rem; }
        dt { }
        dd { margin: 0; }
    </style>
</head>
<body>
<h1><c:out value="${product.name}"/></h1>
<dl>
    <dt>Id</dt>
    <dd>${product.id}</dd>
    <dt>Price</dt>
    <dd><fmt:formatNumber value="${product.price}" minFractionDigits="2" maxFractionDigits="2"/> ₽</dd>
    <dt>Categories</dt>
    <dd><c:out value="${fn:join(product.categories, ', ')}"/></dd>
</dl>
<p>
    <a href="${ctx}/products">To list</a> |
    <a href="${ctx}/products/add">Add some more</a>
</p>
<form action="${ctx}/products/delete/${product.id}" method="post">
    <button type="submit">Delete</button>
</form>
</body>
</html>