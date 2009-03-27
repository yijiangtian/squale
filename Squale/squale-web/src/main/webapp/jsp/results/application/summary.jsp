<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@taglib uri="http://www.airfrance.fr/welcom/tags-welcom" prefix="af"%>
<%@taglib uri="/squale" prefix="squale"%>

<%@ page import="com.airfrance.welcom.struts.util.WConstants"%>
<%@ page
	import="com.airfrance.squaleweb.applicationlayer.formbean.LogonBean"%>
<%@ page import="com.airfrance.squaleweb.resources.WebMessages"%>
<%@ page import="com.airfrance.squaleweb.util.graph.GraphMaker"%>
<%@ page
	import="com.airfrance.squaleweb.applicationlayer.formbean.results.ResultListForm"%>
<%@ page import="com.airfrance.squaleweb.tagslib.HistoryTag"%>

<%
            // R�cup�ration de l'utilisateur en session pour savoir si celui-ci est administrateur
            // SQUALE
            LogonBean sessionUser = (LogonBean) request.getSession().getAttribute( WConstants.USER_KEY );
            boolean isAdmin = sessionUser.isAdmin();
%>

<%
            //Parameter indicates which tab must be selected
            String selectedTab = request.getParameter( HistoryTag.SELECTED_TAB_KEY );
            if ( selectedTab == null )
            {
                // First tab by default
                selectedTab = "factors";
            }
%>

<%
            // Le toolTip pour le pieChart
            String imageDetails = WebMessages.getString( request, "image.piechart" );
%>

<bean:define id="applicationId" name="resultListForm"
	property="applicationId" type="String" />
<bean:define id="applicationName" name="resultListForm"
	property="applicationName" type="String" />
<bean:define id="currentAuditId" name="resultListForm"
	property="currentAuditId" type="String" />
<bean:define id="previousAuditId" name="resultListForm"
	property="previousAuditId" type="String" />
<bean:define id="comparable" name="resultListForm"
	property="comparableAudits" type="Boolean" />

<bean:define id="callbackUrlApp">
	<html:rewrite
		page="//add_applicationTag.do?action=findTagForAutocomplete" />
</bean:define>

<script type="text/javascript"
	src="theme/charte_v03_001/js/tagManagement.js"></script>
<script type="text/javascript" src="jslib/jquery.js"></script>

<af:page titleKey="application.results.title"
	titleKeyArg0="<%=applicationName%>">
	<af:head>
		<%-- inclusion pour le marquage XITI --%>
		<jsp:include page="/jsp/xiti/xiti_header_common.jsp" />
	</af:head>
	<af:body canvasLeftPageInclude="/jsp/canvas/application_menu.jsp">

		<af:canvasCenter>
			<%-- inclusion pour le marquage XITI sp�cifique � la page--%>
			<jsp:include page="/jsp/xiti/xiti_body_common.jsp">
				<jsp:param name="page" value="Consultation::Application" />
			</jsp:include>

			<br />
			<br />
			<squale:resultsHeader name="resultListForm" displayComparable="true">
				<div id="tagRemoval">
					<af:form action="application.do">
						<div id="appTagRemoval" style="visibility:hidden">
							<div id="hidden" style="display:none;">
								<af:field key="empty" property="applicationId" value='<%= applicationId%>'/>
							</div>
							<table>
								<tr>
									<td>
										<bean:define id="listtag" name="resultListForm" property="tags"></bean:define>
										<af:select property="tagDel">
											<af:options collection="listtag" property="name"/>
										</af:select>
									</td>
									<td>
										<af:button callMethod="removeTag" name="supprimer"/>
									</td>
								</tr>
							</table>
						</div>
					</af:form>
				</div>
				<div id="tagAddition">
					<div id="appTagAddition" style="visibility:hidden">
						<af:form action='<%="application.do?action=addTag&applicationId=" + applicationId + "&currentAuditId=" + currentAuditId%>'>
							<af:field key="empty" name="resultListForm" property="tagSupp"
								value="" easyCompleteCallBackUrl="<%=callbackUrlApp%>"/>
						</af:form>
					</div>
				</div>
				<%-- FINISH THIS UP ! https://project.squale.org/ticket/140
				<logic:present name="unexistingTag" scope="request">
					<div id="unexistingTagBox" title="Tag does not exist">
						<bean:write name="unexistingTag"/>
					</div>
					<script>
						showErrorModalBox("unexistingTagBox");
					</script>
				</logic:present>
				--%>
			</squale:resultsHeader>
			<br />
			<h2><bean:message key="application.results.summary.subtitle" /></h2>
			<br />
			<af:tabbedPane name="applicationsummary">
				<%-- Factors --%>
				<af:tab key="application.results.factors.tab" name="factors"
					lazyLoading="false"
					isTabSelected='<%=""+selectedTab.equals("factors")%>'>
					<af:dropDownPanel titleKey="buttonTag.menu.aide">
						<bean:message key="application.results.factors" />
						<br />
						<br />
					</af:dropDownPanel>
					<logic:iterate id="gridResult" name="resultListForm"
						property="list">
						<%-- Si la grille n'a pas de nom, on affiche pas le projet car cela veut dire
							qu'il n'y a pas de r�sultats --%>
						<logic:notEqual name="gridResult" property="gridName" value="">
							<center>
							<h3><bean:message key="project_creation.field.quality_grid" />
							<bean:write name="gridResult" property="gridName" /></h3>
							</center>
							<af:table name="gridResult" property="results" scope="page"
								totalLabelPos="none" emptyKey="table.results.none"
								displayNavigation="false">
								<%
								String paramsLink = "&currentAuditId=" + currentAuditId + "&previousAuditId=" + previousAuditId;
								%>
								<%
								String projectLink = "project.do?action=select" + paramsLink;
								%>
								<%
								String factorLink = "project.do?action=factor" + paramsLink;
								%>
								<af:cols id="element">
									<af:col property="name" key="project.name" sortable="false"
										href="<%=projectLink%>" paramName="element"
										paramId="projectId" paramProperty="id" width="15%" />
									<logic:iterate id="factor" name="element" property="factors"
										indexId="index">
										<bean:define id="factorName" name="factor" property="name"
											type="String" />
										<bean:define id="factorId" name="factor" property="id" />
										<bean:define id="projectId" name="element" property="id" />
										<af:col property='<%="factors[" + index + "].currentMark"%>'
											key="<%=factorName%>">
											<%
											String link = factorLink + "&projectId=" + projectId.toString() + "&which=" + factorId.toString();
											%>
											<a href="<%=link%>" class="nobottom"> <squale:mark
												name="factor" mark="currentMark" /> <squale:trend
												name="factor" current="currentMark"
												predecessor="predecessorMark" /> <squale:picto
												name="factor" property="currentMark" /> </a>
										</af:col>
									</logic:iterate>
								</af:cols>
							</af:table>
						</logic:notEqual>
					</logic:iterate>
				</af:tab>
				<%-- Kiviat --%>
				<af:tab key="application.results.kiviat.tab" name="kiviat"
					lazyLoading="false"
					isTabSelected='<%=""+selectedTab.equals("kiviat")%>'>
					<bean:define id="srcKiviat" name="resultListForm"
						property="kiviat.srcName" type="String" />
					<bean:define id="imgMapKiviat" name="resultListForm"
						property="kiviat.useMapName" type="String" />
					<%-- ligne necessaire --%>
					<bean:define id="description" name="resultListForm"
						property="kiviat.mapDescription" type="String" />
					<%=( (GraphMaker) ( (ResultListForm) ( request.getSession().getAttribute( "resultListForm" ) ) ).getKiviat() ).getMapDescription()%>
					<html:img src="<%=srcKiviat%>" usemap="<%=imgMapKiviat%>"
						border="0" />
					<br />
					<b><bean:message key="image.legend" /></b>
					<br />
					<bean:message key="application.results.kiviat" />
					<af:form action="application.do?action=select" scope="session"
						method="POST">
						<%-- on passe le param�tre applicationId en cach� --%>
						<input name="applicationId" value="<%=applicationId%>"
							type="hidden">
						<input name="currentAuditId" value="<%=currentAuditId%>"
							type="hidden">
						<input name="previousAuditId" value="<%=previousAuditId%>"
							type="hidden">
						<logic:equal name="resultListForm"
							property="displayCheckBoxFactors" scope="session" value="true">
							<table class="formulaire" cellpadding="0" cellspacing="0"
								border="0" width="100px">
								<tr>
									<%-- nom du tab � afficher (kiviat) apr�s soumission de la requ�te --%>
									<input type="hidden" name="<%=HistoryTag.SELECTED_TAB_KEY%>"
										value="kiviat" />
									<af:field key="application.results.allFactors"
										name="resultListForm" property="allFactors" type="CHECKBOX"
										styleClassLabel="td1" styleClass="normal" />
								</tr>
							</table>
							<af:buttonBar>
								<af:button type="form" name="valider" />
							</af:buttonBar>
						</logic:equal>
					</af:form>
				</af:tab>
				<%-- Piechart --%>
				<af:tab key="application.results.volumetry.tab" name="piechart"
					lazyLoading="false"
					isTabSelected='<%=""+selectedTab.equals("piechart")%>'>>
					<br />
					<bean:define id="srcPieChart" name="resultListForm"
						property="pieChart.srcName" type="String" />
					<bean:define id="imgMapPieChart" name="resultListForm"
						property="pieChart.useMapName" type="String" />
					<%-- ligne necessaire --%>
					<%=( (GraphMaker) ( (ResultListForm) ( request.getSession().getAttribute( "resultListForm" ) ) ).getPieChart() ).getMapDescription()%>
					<html:img src="<%=srcPieChart%>" usemap="<%=imgMapPieChart%>"
						border="0" />
					<br />
					<b><bean:message key="image.legend" /></b>
					<br />
					<bean:message key="application.results.piechart" />
				</af:tab>
			</af:tabbedPane>
			<br />
			<bean:size name="resultListForm" property="list" id="resultsSize" />
			<%-- 
				On exporte pas les r�sultats si il n'y a pas de r�sultats m�me si les 
				graphiques sont pr�sents car l'export PDF donnera une page blanche dans ce cas.
			--%>
			<logic:greaterThan name="resultsSize" value="0">
				<h2><bean:message key="exports.title" /></h2>
				<br />
				<af:buttonBar>
					<af:button type="form" name="export.pdf"
						onclick='<%="application.do?action=exportPDF&currentAuditId=" + currentAuditId + "&previousAuditId=" + previousAuditId%>'
						toolTipKey="toolTip.export.pdf.application.result" />
					<%
					            if ( isAdmin )
					            {
					%>
					<af:button type="form" name="export.audit_report"
						onclick='<%="param_audit_report.do?action=param&applicationId=" + applicationId + "&currentAuditId=" + currentAuditId + "&previousAuditId=" + previousAuditId + "&comparable="+comparable.toString()%>'
						toolTipKey="toolTip.export.audit_report" accessKey="admin" />
					<%
					} //isAdmin
					%>
				</af:buttonBar>
			</logic:greaterThan>
		</af:canvasCenter>
	</af:body>
</af:page>