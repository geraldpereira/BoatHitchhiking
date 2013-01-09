function callNoPropagation(calledFunction, e) {
	var event = e || window.event;

	calledFunction();

	if (event.stopPropagation) {
		event.stopPropagation();
	} else {
		event.cancelBubble = true;
	}
}

function changeRichTab( tabPanelID, tabID ,tabName)
{
	var fullIDForTabPanel = jQuery("[id$=" + ":"+ tabPanelID + "]").attr("id");
	var fullIDForTab = jQuery("[id$=" + ":" + tabID + "]").attr("id");
	RichFaces.switchTab(fullIDForTabPanel, fullIDForTab, tabName);
}

var waitDialogShown = false;
var useTimerBeforeShowWaitDialog = true;
var waitDialogTimeout = 50;
var waitDialogTimer;

function showWaitDialog() {
	//avoid attempt to show it if it is already shown
	if (!waitDialogShown) {
		Richfaces.showModalPanel('wait-dialog-invisible');
		waitDialogShown = true;
	}
}

function onRequestStart() {
	if (useTimerBeforeShowWaitDialog) {
		waitDialogTimer = setTimeout("showWaitDialog();", waitDialogTimeout);
	} else {
		showWaitDialog();
	}
}
function onRequestEnd() {
	if (waitDialogShown) {
		Richfaces.hideModalPanel('wait-dialog-invisible');
		waitDialogShown = false;
	} else if (useTimerBeforeShowWaitDialog && waitDialogTimer) {
		clearTimeout(waitDialogTimer);
	}
}
