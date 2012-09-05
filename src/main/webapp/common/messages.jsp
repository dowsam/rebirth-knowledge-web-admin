<%@ include file="/common/taglibs.jsp"%>
<%-- Error Messages --%>
<c:if test="${flash.success!=null}">
	<p class="message">${flash.success}</p>
</c:if>

<%-- Info Messages --%>
<c:if test="${flash.error != null}">
	<p class="error">${flash.error}</p>
</c:if>