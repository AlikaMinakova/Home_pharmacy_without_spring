<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ru">
<head>
    <%-- Подключение общего header --%>
    <jsp:include page="/fragments/header.jsp"/>
</head>
<body>
<div class="container my-5 content">
    <h3 class="fw-bold mb-4">Аптечка</h3>

    <!-- сортировка -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <span class="fw-semibold">СОРТИРОВАТЬ:</span>
        <form action="${pageContext.request.contextPath}/pharmacies" method="get" class="ms-3">
            <select name="sort" class="form-select w-auto" onchange="this.form.submit()">
                <option value="purchaseDate" ${sort == 'purchaseDate' ? 'selected' : ''}>По дате покупки</option>
                <option value="expirationDate" ${sort == 'expirationDate' ? 'selected' : ''}>По сроку годности</option>
                <option value="quantity" ${sort == 'quantity' ? 'selected' : ''}>По количеству</option>
            </select>
            <input type="hidden" name="keyword" value="${keyword}">
        </form>
    </div>

    <!-- список -->
    <div class="row row-cols-1 row-cols-md-3 row-cols-lg-4 g-4 mb-5">
        <c:forEach var="pharmacy" items="${pharmacyPage.all}">
            <div class="col">
                <div class="card h-100 shadow-sm">
                    <c:if test="${not empty pharmacy.medication.image}">
                        <img src="${pageContext.request.contextPath}/uploads${pharmacy.medication.image}"
                             class="card-img-fixed" alt="Фото лекарства">
                    </c:if>
                    <div class="card-body">
                        <p class="text-success small fw-semibold">Есть в аптечке</p>
                        <h6 class="fw-bold">
                            <a href="${pageContext.request.contextPath}/pharmacies/${pharmacy.id}"
                               class="text-decoration-none text-dark">
                                    ${pharmacy.medication.name}
                            </a>
                        </h6>
                        <ul class="list-unstyled small text-muted">
                            <li>Дата покупки: ${pharmacy.purchaseDate}</li>
                            <li>Срок годности: ${pharmacy.expirationDate}</li>
                            <li>Количество: ${pharmacy.quantity} шт</li>
                        </ul>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Купили недавно -->
    <h4 class="fw-bold mb-4">Купили недавно</h4>
    <div class="row row-cols-1 row-cols-md-3 row-cols-lg-4 g-4 mb-5">
        <c:forEach var="pharmacy" items="${pharmacyPage.recentlyBought}">
            <div class="col">
                <div class="card h-100 shadow-sm">
                    <c:if test="${not empty pharmacy.medication.image}">
                        <img src="${pageContext.request.contextPath}/uploads${pharmacy.medication.image}"
                             class="card-img-fixed" alt="Фото лекарства">
                    </c:if>
                    <div class="card-body">
                        <p class="text-success small fw-semibold">Есть в аптечке</p>
                        <h6 class="fw-bold">
                            <a href="${pageContext.request.contextPath}/pharmacies/${pharmacy.id}"
                               class="text-decoration-none text-dark">
                                    ${pharmacy.medication.name}
                            </a>
                        </h6>
                        <ul class="list-unstyled small text-muted">
                            <li>Дата покупки: ${pharmacy.purchaseDate}</li>
                            <li>Срок годности: ${pharmacy.expirationDate}</li>
                            <li>Количество: ${pharmacy.quantity} шт</li>
                        </ul>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Заканчивается срок годности -->
    <h4 class="fw-bold mb-4">Заканчивается срок годности</h4>
    <div class="row row-cols-1 row-cols-md-3 row-cols-lg-4 g-4 mb-5">
        <c:forEach var="pharmacy" items="${pharmacyPage.expiringSoon}">
            <div class="col">
                <div class="card h-100 shadow-sm">
                    <c:if test="${not empty pharmacy.medication.image}">
                        <img src="${pageContext.request.contextPath}/uploads${pharmacy.medication.image}"
                             class="card-img-fixed" alt="Фото лекарства">
                    </c:if>
                    <div class="card-body">
                        <p class="text-success small fw-semibold">Есть в аптечке</p>
                        <h6 class="fw-bold">
                            <a href="${pageContext.request.contextPath}/pharmacies/${pharmacy.id}"
                               class="text-decoration-none text-dark">
                                    ${pharmacy.medication.name}
                            </a>
                        </h6>
                        <ul class="list-unstyled small text-muted">
                            <li>Дата покупки: ${pharmacy.purchaseDate}</li>
                            <li>Срок годности: ${pharmacy.expirationDate}</li>
                            <li>Количество: ${pharmacy.quantity} шт</li>
                        </ul>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

</body>
</html>
