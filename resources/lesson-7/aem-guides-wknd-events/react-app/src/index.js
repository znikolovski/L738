import React from 'react';
import ReactDOM from 'react-dom';
import { ModelManager, Constants } from '@adobe/cq-spa-page-model-manager';
import './index.scss';
import App from './App';
import "./components/MappedComponents";
import ScrollToTop from './utils/RouteHelper';
import {BrowserRouter} from 'react-router-dom';
import { Redirect, Route } from 'react-router';

function render(model, useHydrate) {
    ReactDOM.render((
        <BrowserRouter>
            <ScrollToTop>
                <Route exact path="/" render={() => (
                    <Redirect to="/content/wknd-events/react/home.html"/>
                )}/>
                <App cqChildren={ model[Constants.CHILDREN_PROP] } cqItems={ model[Constants.ITEMS_PROP] } cqItemsOrder={ model[Constants.ITEMS_ORDER_PROP] }
                    cqPath={ ModelManager.rootPath } locationPathname={ window.location.pathname }/>
            </ScrollToTop>
        </BrowserRouter>), 
        document.getElementById('root'));
}

document.addEventListener('DOMContentLoaded', () => {
    let jsonScript = document.getElementById("__INITIAL_STATE__");
    let initialState = null;
    if (jsonScript) {
        initialState = JSON.parse(jsonScript.innerText);
        // Remove the script element from the DOM
        jsonScript.remove();
    }

    let initialModel = initialState ? initialState.rootModel : undefined;

    ModelManager.initialize({path: process.env.REACT_APP_PAGE_MODEL_PATH, model: initialModel}).then((model) => {
        render(model, !!initialModel);
    });
});