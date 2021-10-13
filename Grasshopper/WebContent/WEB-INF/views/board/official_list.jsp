<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:import url="/WEB-INF/views/layout/header.jsp" />

<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>    
<link rel="stylesheet" type="text/css" href="/resources/css/cards.css">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
      rel="stylesheet">
      
<!-- 부트스트랩 3.3.2 -->
<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">

<!-- 부가적인 테마 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">

<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

<!-- 검색창 -->
<div class="search-bar">
	<form name="searchForm" action="/official/list" method="get">
	<select id="category" name="category" value="${category }">
   		<option value="all">전체</option>
   		<option value="name">이름</option>
   		<option value="detail">내용</option>
   		<option value="ingred">재료</option>
  	</select>
	<input type="text" name="search" value="${search }" autocomplete="off" 
	id="search" onkeyup="searchFunction()" placeholder="검색어를 입력하세요">
	<input type="submit" id="btnSearch" value="찾기">
	</form>
</div>


<div class="main">

<!-- 카드보드형태 -->
<div class="cards" id="card">
	<c:forEach var="o" items="${list }">
	<div class="card">
	<div class="card_image">
		<img src="/resources/img/${o.official_cocktail_name }.jpg" />
	</div>
	<div class="card_title">
		<h3>${o.official_cocktail_name }</h3>
	</div>
	<div class="card_desc">
		<p>${o.official_cocktail_detail }</p>
	</div>
	<div class="card_info">
		<div>
			<i class="material-icons">thumb_up</i> ${o.official_cocktail_vote }
		</div>
		<div>
			<a class="card_link" href="/official/view?official_no=${o.official_cocktail_no }">Read More...</a>
		</div>
	</div>
	</div>
	</c:forEach>
</div>
</div>

<%-- 맨위로 버튼 --%>
<button id="topButton" onclick="toTheTop()">맨 위로</button>

<!-- 최상단으로 버튼 스크립트 -->
<script type="text/javascript">
var topButton = document.getElementById("topButton");
//스크롤 시 scrollFunction 동작
window.onscroll = function() {
	scrollFunction()	
};
//버튼이 나타나게 함
function scrollFunction(){
	console.log("[TEST] You are scrolling!") //테스트용
	//스크롤을 통해 20줄 이상 내려가면 
	if(document.body.scrollTop > 20 || document.documentElement.scrollTop > 20 ){
		topButton.style.display = "block";	
	} else {
		topButton.style.display = "none";
	}
}
//화면 최상단으로 보내는 기능
function toTheTop(){
	window.scrollTo(0,0);
}
</script>

<c:import url="/WEB-INF/views/layout/official_paging.jsp" />
<c:import url="/WEB-INF/views/layout/footer.jsp" />    