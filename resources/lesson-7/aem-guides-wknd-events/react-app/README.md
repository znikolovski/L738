This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

Below you will find some information on how to perform common tasks.<br>
You can find the most recent version of this guide [here](https://github.com/facebook/create-react-app/blob/master/packages/react-scripts/template/README.md).

## Table of Contents

- [Available Scripts](#available-scripts)
  - [npm start](#npm-start)
  - [npm run build](#npm-run-build)
  - [npm run styleguide](#npm-run-style-guide)
  

## Available Scripts

In the project directory, you can run:

### `npm start`

There are two possible ways to run the app in development mode. Both are commented in `.env.development` for reference.

**Proxy** the JSON model from AEM:

1. Start the development server:

    ```
    $ REACT_APP_PAGE_MODEL_PATH=/content/wknd-events/react.model.json npm start
    ```

2. Open a browser window and login to AEM at http://localhost:4502
3. Open a new tab in the same browser and navigate to [http://localhost:3000/content/wknd-events/react/home.html](http://localhost:3000/content/wknd-events/react/home.html)

**Mock** the JSON model locally:

1. Start the development server:

    ```
    $  REACT_APP_PAGE_MODEL_PATH=mock.model.json npm start
    ```
2. Open the browser and navigate to [http://localhost:3000/content/wknd-events/react/home.html](http://localhost:3000/content/wknd-events/react/home.html)

In both setups the page will reload if you make edits.<br>
You will also see any lint errors in the console.

### `npm run build`

Builds the app for production to the `build` folder.<br>
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br>
Your app is ready to be deployed!

### SSR Setup

0. Modify the page component HTL template [a,b]
1. Start the local NODE.js server [c]
2. Request the page from your browser
3. Using a web browser, navigate to one of the pages of the app

a. Location of the page component HTL body template
```
../content/jcr_root/apps/wknd-events/components/structure/page/body.html
```

b. Expected content of the page component HTL body template
```
<app-root id="root">
    <sly data-sly-resource="${resource @ resourceType='cq/remote/content/renderer'}" />
</app-root>
```

c. Command to start the local node server
```
npm run start:server
```

d. Configure the AEM Remote HTML Renderer Servlet

0. Navigate to the System OSGi Configuration console (http://localhost:4502/system/console/configMgr)
1. Look for the configuration named `Remote HTML renderer configuration Factory`
2. Create a new configuration and set the after-mentioned fields as follow

```
Content path pattern=/content/wknd-events/react(.*)
Remote endpoint URL=http://localhost:4200 
``` 


