<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
    <title> Products </title>
    <style>
    </style>
</head>
<body>
    <h1> Products </h1>

    <table>
        <thead>
            <tr>
                <th>Name</th>
                <th>Price</th>
                <th>Categories</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${products}" var="product">
            <tr>
                <td><a href="${ctx}/products/${product.id}"><c:out value="${product.name}"/></a></td>
                <td><fmt:formatNumber value="${product.price}" minFractionDigits="2" maxFractionDigits="2"/> ₽</td>
                <td><c:out value="${fn:join(product.categories, ', ')}"/></td>
                <td>
                    <form action="${ctx}/products/delete/${product.id}" method="post">
                        <button type="submit">Delete</button>
                    </form>
                </td>
            </tr>
            </c:forEach>
        </tbody>
    </table>

    <h2> By category </h2>
    <table>
        <thead>
            <tr>
                <th>Category</th>
                <th>Count</th>
                <th>Total</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${stats}" var="entry">
            <tr>
                <td><c:out value="${entry.key}"/></td>
                <td>${entry.value.count}</td>
                <td><fmt:formatNumber value="${entry.value.total}" minFractionDigits="2" maxFractionDigits="2"/> ₽</td>
            </tr>
            </c:forEach>
        </tbody>
    </table>

    <h2> Add products </h2>
    <a href="${ctx}/products/add"> add </a>
</body>
</html>