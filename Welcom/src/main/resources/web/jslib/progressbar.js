function wPBarPreExecTaskForm(jsCompProgressId,jsCaller){razProgressBar(jsCompProgressId);var descPrg;var initURL="runTaskAction.do?action=registerTask";var xmlResp=httpGetSyncronizedURL(initURL,'XML');descPrg=processInitialRequest(jsCompProgressId,xmlResp);if(descPrg!=null){document.getElementById("wWatchedTaskId").value=descPrg.idProgress;if(document.getElementById(descPrg.jsCompId).getAttribute('wIsFullScreen')=='true'){var winW=1024,winH=780;if(parseInt(navigator.appVersion)>3){if(navigator.appName=="Netscape"){winW=window.innerWidth;winH=window.innerHeight;}if(navigator.appName.indexOf("Microsoft")!=-1){winW=document.body.offsetWidth;winH=document.body.offsetHeight;}}else{winW=window.innerWidth;winH=window.innerHeight;}document.getElementById("wDivProgressBarBG").style.width=winW+'px';document.getElementById("wDivProgressBarBG").style.height=winH+'px';document.getElementById("wDivProgressBarBG").style.visibility='visible';document.getElementById("wDivProgressBar").style.visibility='visible';}if(jsCaller!=null){if(jsCaller.href.indexOf("\?")!=-1){if(jsCaller.href.charAt(jsCaller.href.length-1)=="&"){jsCaller.href=jsCaller.href+"wWatchedTaskId="+descPrg.idProgress;}else{jsCaller.href=jsCaller.href+"&wWatchedTaskId="+descPrg.idProgress;}}else{jsCaller.href=jsCaller.href+"?wWatchedTaskId="+descPrg.idProgress;}}if(navigator.userAgent.indexOf("Safari")!=-1){document.getElementById("wImgProgressBar").src="img.do?value=img/pb_run.gif"}else{setTimeout("checkProgress("+descPrg.idProgress+", 0)",200);}}else{return false;}}function wPBarPreExecTaskLink(jsCompProgressId,jsCaller,calledURL){razProgressBar(jsCompProgressId);var descPrg;var initURL="runTaskAction.do?action=registerTask&wWatchedAction="+calledURL;var xmlResp=httpGetSyncronizedURL(initURL,'XML');descPrg=processInitialRequest(jsCompProgressId,xmlResp);if(descPrg!=null){document.getElementById("wWatchedTaskId").value=descPrg.idProgress;if(document.getElementById(descPrg.jsCompId).getAttribute('wIsFullScreen')=='true'){var winW=1024,winH=780;if(parseInt(navigator.appVersion)>3){if(navigator.appName=="Netscape"){winW=window.innerWidth;winH=window.innerHeight;}if(navigator.appName.indexOf("Microsoft")!=-1){winW=document.body.offsetWidth;winH=document.body.offsetHeight;}}else{winW=window.innerWidth;winH=window.innerHeight;}document.getElementById("wDivProgressBarBG").style.width=winW+'px';document.getElementById("wDivProgressBarBG").style.height=winH+'px';document.getElementById("wDivProgressBarBG").style.visibility='visible';document.getElementById("wDivProgressBar").style.visibility='visible';}if(navigator.userAgent.indexOf("Safari")!=-1){document.getElementById("wImgProgressBar").src="img.do?value=img/pb_run.gif"}else{checkProgress(descPrg.idProgress,0);}runTaskFwd(descPrg.idProgress);}else{return false;}}function wExecPBarTask(taskClass,taskParams,jsCompProgressId,refRate){razProgressBar(jsCompProgressId);var descPrg;var initURL="runTaskAction.do?action=execSchedTask&wWatchedAction="+taskClass+'&'+taskParams;var xmlResp=httpGetSyncronizedURL(initURL,'XML');descPrg=processInitialRequest(jsCompProgressId,xmlResp);descPrg.action=taskClass;setTimeout("checkProgress('"+descPrg.idProgress+"', 0)",document.getElementById(descPrg.jsCompId).getAttribute('wRefreshRate'));}function runTaskFwd(taskId){var runURL="runTaskAction.do?action=execTaskForward&wWatchedTaskId="+taskId;var ajxRequest=getHTTPObject();ajxRequest.open("GET",runURL,true);ajxRequest.onreadystatechange=function(){finExecHook(ajxRequest)};ajxRequest.send("");}function finExecHook(ajxRequest){if(ajxRequest.readyState==4){document.write(ajxRequest.responseText);document.close();}}function razProgressBar(jsCompProgressId){if(navigator.userAgent.indexOf("Safari")!=-1){document.getElementById("wImgProgressBar").src="img.do?value=img/pb_ini.gif"}else{}}function processInitialRequest(jsCompProgressId,xmlResp){var res;try{res=parseAjaxResponse(xmlResp);}catch(e){showError(e.description);return null;}var descPrg=new DescProgress(res.id,jsCompProgressId,'NOT_SET');infosProgress.put(res.id,descPrg,res.status);if(res.errors!=''){showError(res);}else{showProgress(res);}return descPrg;}function checkProgress(taskId,oldValue){var url="runTaskAction.do?action=checkProgress&wOldProgressValue="+oldValue+"&wWatchedTaskId="+taskId;var ajxRequest=getHTTPObject();ajxRequest.open("GET",url,true);ajxRequest.onreadystatechange=function(){progressInfoHook(ajxRequest);};ajxRequest.send("");}function progressInfoHook(ajxRequest){try{if(ajxRequest.readyState==4){if(ajxRequest.status==200){if(ajxRequest.responseXML!=null){var res=parseAjaxResponse(ajxRequest.responseXML);if(res.errors!=''){showError(res);}else{showProgress(res);var pBarInfo=infosProgress.get(res.id);if(res.progress<100){setTimeout("checkProgress('"+res.id+"', '"+res.progress+"')",document.getElementById(pBarInfo.jsCompId).getAttribute('wRefreshRate'));}else{infosProgress.remove(res.id);var completeHook=document.getElementById(pBarInfo.jsCompId).getAttribute('wOnCompleteHook');if(completeHook!=null&&completeHook!="null"){setTimeout(completeHook+"('"+pBarInfo.jsCompId+"', '"+res.status+"', '"+res.errors+"')",document.getElementById(pBarInfo.jsCompId).getAttribute('wRefreshRate'));}}}}else{showError("Bad response format");throw"responseXML is null";}}}}catch(e){alert("Communication error : "+e.description);throw e;}}function parseAjaxResponse(xmlResponse){var obj;var item=xmlResponse.getElementsByTagName("confirm")[0];var taskId=item.getElementsByTagName("taskid")[0].firstChild.nodeValue;var taskPct=item.getElementsByTagName("progress")[0].firstChild.nodeValue;var taskStatus="";obj=item.getElementsByTagName("status");if(obj.length>0){taskStatus=item.getElementsByTagName("status")[0].firstChild.nodeValue;}var taskErrors="";obj=item.getElementsByTagName("errors");if(obj.length>0){taskErrors=item.getElementsByTagName("errors")[0].firstChild.nodeValue;}var result=new AjaxMessage(taskId,taskPct,taskStatus,taskErrors);return result;}function showProgress(infos){if(navigator.userAgent.indexOf("Safari")!=-1){return;}var pbBar1;var pbBar2;var pBarInfo=infosProgress.get(infos.id);var prgBar=pBarInfo.jsCompId;var stat=document.getElementById(prgBar+"_status");if((stat!=null)&&(stat!='undefined')){stat.innerHTML=infos.status;}var txt=document.getElementById(prgBar+"_pctText");if((txt!=null)&&(txt!='undefined')){txt.innerHTML=infos.progress+"%";}pbBar1=document.getElementById(prgBar+"_all_td1");if(infos.progress<100){pbBar1.style.width=infos.progress+'%';}var updateHook=document.getElementById(pBarInfo.jsCompId).getAttribute('wOnUpdateHook');if(updateHook!=null&&updateHook!="null"){try{setTimeout(updateHook+"('"+pBarInfo.jsCompId+"', '"+infos.progress+"', '"+infos.status+"')",10);}catch(e){}}}function showError(res){try{var pBarInfo=infosProgress.get(res.id);infosProgress.remove(res.id);var completeHook=document.getElementById(pBarInfo.jsCompId).getAttribute('wOnCompleteHook');if(completeHook!=null&&completeHook!="null"){setTimeout(completeHook+"('"+pBarInfo.jsCompId+"', '"+res.status+"', '"+res.errors+"')",10);}var stat=document.getElementById(pBarInfo.jsCompId+"_status");stat.innerHTML='error !';}catch(e){alert("Unexpected error parsing server response :"+res);}}function AjaxMessage(taskId,progress,status,errors){this.id=taskId;this.progress=progress;this.status=status;this.errors=errors;}function DescProgress(idProgress,jsCompId,action){this.idProgress=idProgress;this.jsCompId=jsCompId;this.action=action;}var infosProgress={put:function(key,val){this[key]=val;},remove:function(key){this[key]=null;},get:function(key){return this[key];}};