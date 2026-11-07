<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title>New product</title>
    <style>
        input { padding: .3rem; width: 18rem; }
        .error { color: #b00; }
    </style>
</head>
<body>
<h1> New product </h1>

<c:if test="${not empty error}">
    <p class="error"><c:out value="${error}"/></p>
</c:if>

<form action="${ctx}/products/add" method="post">
  <label for="name">Name</label>
  <input id="name" name="name" type="text" value="${fn:escapeXml(product.name)}"/>

  <label for="price">Price</label>
  <input id="price" name="price" type="text" value="${fn:escapeXml(product.price)}"/>

  <label for="categories"> Categories </label>
  <input id="categories" name="categories" type="text" value="${fn:escapeXml(fn:join(product.categories, ', '))}"/>

  <p>
        <button type="submit">Save</button>
        <a href="${ctx}/products">Dispatch</a>
    </p>
</form>
</body>
</html>