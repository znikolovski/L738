
# L738 - No Author Left Behind - Develop SPAs That Are Fast, Engaging, and Editable

## Table of Contents

* [Lab Overview](#lab-overview) 10 minutes
* [Lesson 1 - SPA Starter Kit](#lesson-1---spa-starter-kit) 10 minutes
* [Lesson 2 - Hello World](#lesson-2---hello-world) 5 minutes
* [Lesson 3 - WKND Events](#lesson-3---wknd-events-app) 5 minutes
* [Lesson 4 - JSON Contract](#lesson-4---json-contract) 5 minutes
* [Lesson 5 - Front-end Development](#lesson-5---front-end-development) 15 minutes
* [Lesson 6 - Back-end Development](#lesson-6---back-end-development) 10 minutes
* [Lesson 7 - Navigation and Routing](#lesson-7---navigation-and-routing) 10 minutes (Discussion, no coding)
* [Lesson 8 - Server Side Rendering](#lesson-8---server-side-rendering) 10 minutes
* [Next Steps](#next-steps)
* [Appendix](#appendix)



## Lab Overview

As brands move to Single Page Applications (SPA), too often the digital marketer has minimal control over content and layout decisions. With the SPA Editor framework in Adobe Experience Manager, digital marketers are no longer left out of the process. Front-end developers continue to build highly performant and rich experiences, while enabling content authors to make in-context editorial updates.

### Key Takeaways

* Use modern web development tools of npm and webpack to streamline front-end developer workflow with traditional AEM 
* Map front-end components in React or Angular to back-end data models in Adobe Experience Manager
* Optimize the application with server-side rendering

### Prerequisites

A local development environment with the following tools and technologies installed:

#### Adobe Experience Manager

A local author instance of AEM should be installed running locally on port 4502

* [AEM 6.5](#)
* [AEM 6.4 + SP2](https://helpx.adobe.com/experience-manager/6-4/release-notes/sp-release-notes.html)

#### Tools

* [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Apache Maven](https://maven.apache.org/) (3.3.9 or newer)
* [Node.js v10+](https://nodejs.org/en/)
* [npm 6+](https://www.npmjs.com/)

#### IDE

The lab will use Visual Studio Code as the IDE and screenshots will be of Visual Studio Code. IntelliJ, Eclipse are also perfectly valid IDEs to work in. 

See [Set up a Local AEM Development Environment](https://helpx.adobe.com/experience-manager/kt/platform-repository/using/local-aem-dev-environment-article-setup.html) for detailed instructions.

* [Visual Studio Code](https://code.visualstudio.com/) with [Repo](https://github.com/Adobe-Marketing-Cloud/tools/tree/master/repo#integration-into-visual-studio-code)

Start by double checking that the above tools have been installed and available via the command line path. 

Open up a new terminal and run the following commands:

```shell
$ java -version
java version "1.8.+"
Java(TM) SE Runtime Environment (build 1.8.0_111-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.111-b14, mixed mode)
  
$ mvn -version
Apache Maven 3.3.9
Maven home: /Library/apache-maven-3.3.9
Java version: 1.8.0_111, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0_111.jdk/Contents/Home/jre
  
$ node --version
v10.8.0
  
$ npm --version
6.2.0
```

## Angular + React

This lab has been written so that participants can complete the lab in either Angular or React frameworks. Throughout the manual you may see indicator icons like the following:

#### ![React](./images/react-logo.png) Exercise xyz (React only)

or

#### ![Angular](./images/angular-logo.png) Exercise xyz (Angular only)

Complete only the exercise that corresponds to the framework you have chosen. 

**Or if you are ambitious do both!**

## Lab Resources

There are a number for files that will be used in the lab. If completing this lab in person, the files are already downloaded to the Desktop under a folder named `resources`. You can also find the same [resources online in the git repository](./resources).

## Lesson 1 - SPA Starter Kit 

### Objective

1. Learn the best practices for starting a new SPA enablement project with the [Maven Archetype for SPA Starter Kit](https://github.com/adobe/aem-spa-project-archetype/tree/master).
2. Learn how a webpack project's build artifacts can be integrated and deployed as an AEM client library.
3. Understand how the plugins of [aem-clientlib-generator](https://www.npmjs.com/package/aem-clientlib-generator) and [frontend-maven-plugin](https://github.com/eirslett/frontend-maven-plugin) are used to automate the build process.

### Lesson Context

SPA development will be done primarily in a webpack project outside of AEM. This offers several advantages:

* Separation of front-end and back-end concerns. 
* Front-end tooling has a very rapid lifecycle. By moving the front-end development outside of AEM, it ensures that the latest and greatest versions of these tools can be used, independent of the version of AEM.

At a high level, the integration approach is as follows: 

1. A webpack production build is triggered.
2. The compiled SPA, the production distribution of CSS and JS, is copied into the **ui.apps** module as an AEM [client library](https://helpx.adobe.com/experience-manager/6-4/sites/developing/using/clientlibs.html)
3. The **ui.apps** is deployed to AEM as part of an AEM package. 

![build deploy high level](./images/build-deploy-highlevel.png)

The concept is similar to the integration of the **core** Java bundle, where the Java bundle is compiled into a jar file that is embedded into the **ui.apps** module and deployed to AEM as an AEM package.

To achieve this integration two tools will be used:

* [aem-clientlib-generator](https://www.npmjs.com/package/aem-clientlib-generator) - used to transform compiled CSS and JS files into an AEM client library
* [frontend-maven-plugin](https://github.com/eirslett/frontend-maven-plugin) - used to trigger NPM commands via a Maven build. This plugin will download/install Node and NPM locally for the project, ensuring consistency and making the project easy to integrate with a Continuous Integration/Continuous Deployment environment.

#### ![React](./images/react-logo.png) Exercise 1.1 - Open the L738 SPA React Project

1. The [Maven Archetype for SPA Starter Kit](https://github.com/adobe/aem-spa-project-archetype/tree/master) was used to create a new SPA project with React. The following [parameters](./resources/lesson-1/React-archetype-params.txt) were used:

    ```
    mvn archetype:generate -B \
        -DarchetypeCatalog=local  \
        -DarchetypeGroupId=com.adobe.cq.spa.archetypes  \
        -DarchetypeArtifactId=aem-spa-project-archetype  \
        -DarchetypeVersion=1.0.3-SNAPSHOT \
        -Dpackage=summitlab \
        -DgroupId=com.adobe.summit \
        -DartifactId=L738-spa-react \
        -Dversion=0.0.1-SNAPSHOT \
        -DprojectTitle="L738 SPA React App"  \
        -DprojectName=L738-react  \
        -DcomponentGroup="L738 React Content" \
        -DoptionFrontend=react
    ```

2. Open **Visual Studio Code** IDE
3. Click **Open Folder** and navigate to **Desktop** > **resources** > **lesson-1** > **L738-spa-react**
![L738-spa-react](./images/l738-spa-react-project.png)
4. There are 5 folders in the project that represent individual Maven modules
    * **all** Combines all modules in a single package
    * **core**: OSGi bundle containing Java code
    * **react-app** React application
    * **ui.apps** - AEM components and dialog definitions
    * **ui.content** - AEM templates and configurations
5.  Beneath the **react-app** folder, open the file: `package.json`. Inspect the **npm** scripts **build** command:

    `"build": "react-scripts build && clientlib --verbose"`

    This will trigger a production build of the react application and then copy the compiled JS and CSS into an AEM Client library.
6. Beneath the **react-app** folder, open the file: `clientlib.config.js`. This is the [aem-clientlib-generator](https://www.npmjs.com/package/aem-clientlib-generator) used to transform the production build of the react app into an AEM client library that will be copied into the **ui.apps** folder.

### ![React](./images/react-logo.png) Exercise 1.2 - Build the L738 SPA React Project

1. In **Visual Studio Code** from menu bar > **Terminal** > **New Terminal**

    ![Open terminal](./images/open-terminal.png)

2. The terminal should default to be in the directory: `~/Desktop/resources/lesson-1/L738-spa-react`.
3. Run the following command in the terminal:

    ```
    $ mvn -PautoInstallPackage clean install
    ```
    This will build and deploy the application to a local instance of AEM running at [http://localhost:4502](http://localhost:4502).

    ```
    [INFO] ------------------------------------------------------------------------
    [INFO] Reactor Summary:
    [INFO]
    [INFO] L738 SPA React App Reactor ......................... SUCCESS [  0.300 s]
    [INFO] L738 SPA React App Core ............................ SUCCESS [  6.849 s]
    [INFO] L738 SPA React App React App ....................... SUCCESS [ 32.572 s]
    [INFO] L738 SPA React App - SPA UI apps ................... SUCCESS [  1.152 s]
    [INFO] L738 SPA React App - SPA UI content ................ SUCCESS [  0.644 s]
    [INFO] L738 SPA React App All-in-One ...................... SUCCESS [  0.885 s]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 43.965 s
    [INFO] Finished at: 2019-01-25T17:27:13-05:00
    [INFO] Final Memory: 55M/714M
    ```
4. Open a new browser and navigate to AEM: [http://localhost:4502](http://localhost:4502).
5. Login using the credentials:
    * User name: **admin**
    * Password: **admin**
6. Click **Tools** > **Deployment** > **Packages** to navigate to CRX Package Manager:

    ![Package Manager](./images/package-manager.png)

7. You should see that the package has been deployed:

    ![L738 React Package](./images/deployed-packagesl738-react.png)


### ![Angular](./images/angular-logo.png) Exercise 1.1 - Open the L738 SPA Angular Project

1. The [Maven Archetype for SPA Starter Kit](https://github.com/adobe/aem-spa-project-archetype/tree/master) was used to create a new SPA project with Angular. The following [parameters](./resources/lesson-1/Angular-archetype-params.txt) were used:

    ```
    mvn archetype:generate -B \
     -DarchetypeCatalog=local  \
     -DarchetypeGroupId=com.adobe.cq.spa.archetypes  \
     -DarchetypeArtifactId=aem-spa-project-archetype  \
     -DarchetypeVersion=1.0.3-SNAPSHOT \
     -Dpackage=summitlab \
     -DgroupId=com.adobe.summit \
     -DartifactId=L738-spa-angular \
     -Dversion=0.0.1-SNAPSHOT \
     -DprojectTitle="L738 SPA Angular App"  \
     -DprojectName=L738-angular  \
     -DcomponentGroup="L738 Angular Content" \
     -DoptionFrontend=angular
    ```

2. Open **Visual Studio Code** IDE
3. Click **Open Folder** and navigate to **Desktop** > **resources** > **lesson-1** > **L738-spa-angular**
![L738-spa-angular](./images/l738-spa-angular-project.png)
4. There are 5 folders in the project that represent individual Maven modules
    * **all** Combines all modules in a single package
    * **core**: OSGi bundle containing Java code
    * **angular-app** Angular application
    * **ui.apps** - AEM components and dialog definitions
    * **ui.content** - AEM templates and configurations
5.  Beneath the **angular-app** folder, open the file: `package.json`. Inspect the **npm** scripts **build** command:

    `"build": "ng build --build-optimizer=false && clientlib"`

    This will trigger a production build of the angular application and then copy the compiled JS and CSS into an AEM Client library.
6. Beneath the **angular-app** folder, open the file: `clientlib.config.js`. This is the [aem-clientlib-generator](https://www.npmjs.com/package/aem-clientlib-generator) used to transform the production build of the angular app into an AEM client library that will be copied into the **ui.apps** folder.

### ![Angular](./images/angular-logo.png) Exercise 1.2 - Build the L738 SPA Angular Project

1. In **Visual Studio Code** from menu bar > **Terminal** > **New Terminal**

    ![Open terminal](./images/open-terminal.png)

2. The terminal should default to be in the directory: `~/Desktop/resources/lesson-1/L738-spa-angular`.
3. Run the following command in the terminal:

    ```
    $ mvn -PautoInstallPackage clean install
    ```
    This will build and deploy the application to a local instance of AEM running at [http://localhost:4502](http://localhost:4502).

    ```
    [INFO] ------------------------------------------------------------------------
    [INFO] Reactor Summary:
    [INFO]
    [INFO] L738 SPA Angular App Reactor ....................... SUCCESS [  0.306 s]
    [INFO] L738 SPA Angular App Core .......................... SUCCESS [  6.495 s]
    [INFO] L738 SPA Angular App Angular App ................... SUCCESS [ 34.384 s]
    [INFO] L738 SPA Angular App - SPA UI apps ................. SUCCESS [  1.922 s]
    [INFO] L738 SPA Angular App - SPA UI content .............. SUCCESS [  0.615 s]
    [INFO] L738 SPA Angular App All-in-One .................... SUCCESS [  1.137 s]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 46.440 s
    [INFO] Finished at: 2019-01-25T17:44:17-05:00
    [INFO] Final Memory: 59M/707M
    ```
4. Open a new browser and navigate to AEM: [http://localhost:4502](http://localhost:4502).
5. Login using the credentials:
    * User name: **admin**
    * Password: **admin**
6. Click **Tools** > **Deployment** > **Packages** to navigate to CRX Package Manager:

    ![Package Manager](./images/package-manager.png)

7. You should see that the package has been deployed:

    ![L738 React Package](./images/deployed-packagesl738-angular.png)

## Lesson 2 - Hello World

### Objective

1. As an AEM author, add a text component to the page and author a new message.
2. Learn where authored content is persisted in the JCR and how this content is exposed as JSON.
3. Understand how a front-end component is mapped to an AEM component based on a Sling resource type.

### Lesson Context

The goal of the SPA Editor is to allow marketers to be able to make in-context edits to a Single Page Application. In this lesson we will trace how authored content is persisted to the JCR and then exposed as JSON. We will then look at how the SPA inspects the JSON to map the data model to a front-end component. The **Text** component which is included out of the box by the SPA Starter Kit will be used.

![Map to](./images/mapto-diagram.png)

#### Exercise 2.1 - Add a text component to the page and author a new message

1. From the AEM Start Menu [http://localhost:4502/aem/start.html](http://localhost:4502/aem/start.html) Navigate to **Sites**.

    ![Sites icon](./images/sites-icon.png)

2. ![React Logo](./images/react-logo.png) Navigate to **L738-react** > **en** > **home** and open the page using the Sites Editor:

    ![Open React Home](./images/lesson-2/open-react-home.png)

    **or** ![Angular Logo](./images/angular-logo.png) Navigate to **L738-angular** > **en** > **home** and open the page using the Sites Editor:
3. Select the existing **Text** component and click the **edit** icon (pencil) to engage the in-line editor.

    ![Edit hello world component](./images/lesson-2/select-hello-world.png)

4. Update the component to read **Hello L738!**

    ![Update component](./images/lesson-2/hello-l738.png)

5. Add a new **Text** Component by opening up the **Content Finder** > **Components** > Drag+Drop a new **Text** Component on to the page:

    ![new component](./images/lesson-2/drag-new-textcomponent.png)

6. Edit the new **Text** component to create another message by clicking the **Wrench** icon ![wrench dialog](./images/lesson-2/wrench-icon.png)

#### Exercise 2.2 - Visualize the persisted data

1. Visualize the persisted data in CRXDE lite by opening a **new tab** and navigating to [http://localhost:4502/crx/de/index.jsp](http://localhost:4502/crx/de/index.jsp)
2. Use the **Navigation Tree** on the left side to navigate to the Home Page
     - ![react-logo](./images/react-logo.png) React: **content** > **L738-react** > **en** > **home** > **jcr:content** > **root** > **responsivegrid**
    - ![angular-logo](./images/angular-logo.png) Angular: **content** > **L738-angular** > **en** > **home** > **jcr:content** > **root** > **responsivegrid**

    ![crxde content tree](./images/lesson-2/crxde-tree.png)

3. Select the first child node prefixed with **text_**. In the properties tab, you can observe the data associated with the text component you created

    ![Text component data persistence](./images/lesson-2/text-properties.png)

4. View the properties of the second **Text** component that was added to the page.

#### Exercise 2.3 - Visualize the serialized data

1. In the browser, return to the Home page:

    - ![react-logo](./images/react-logo.png) React: [http://localhost:4502/editor.html/content/L738-react/en/home.html](http://localhost:4502/editor.html/content/L738-react/en/home.html)
    - ![angular-logo](./images/angular-logo.png) Angular: [http://localhost:4502/editor.html/content/L738-angular/en/home.html](http://localhost:4502/editor.html/content/L738-angular/en/home.html)

2. Click the **Page Properties** menu bar and select **View as Published**:

    ![view as published](./images/lesson-2/view-properties.png)

3. Right + Click the page to view the page's source:

    ![view source](./images/lesson-2/view-source.png)

4. When viewing the source, **note** that there is no HTML that represent the **Text** components added to the page. This is because the components are generated via the React or Angular Javascript application. **Note** the inclusion of the script tag at the bottom of the page. This loads the compiled React or Angular application:

    ```html
    <!-- React page source -->
    <script type="text/javascript" src="/etc.clientlibs/L738-react/clientlibs/L738-react-react.js"></script>
    ```

    ```html
    <!-- Angular page source -->
    <script type="text/javascript" src="/etc.clientlibs/L738-angular/clientlibs/L738-angular-angular.js"></script>
    ```
5. Change the extension of the home page by adding **.model.json** selectors to view the serialized json. Search for the keyword `text` to find the JSON output of the **text** components:

    - ![react-logo](./images/react-logo.png) React: [http://localhost:4502/content/L738-react/en/home.model.json](http://localhost:4502/content/L738-react/en/home.model.json)

    ```json

    ":items": {
        "text": {
            "text": "<p>Hello <b>L738</b>!</p>\n",
            "richText": true,
            ":type": "L738-react/components/text"
        },
        "text_382981548": {
            "text": "<h1>My first Heading</h1>\n",
            "richText": true,
            ":type": "L738-react/components/text"
        }
    }
    ```

    - ![angular-logo](./images/angular-logo.png) Angular: [http://localhost:4502/content/L738-angular/en/home.model.json](http://localhost:4502/content/L738-angular/en/home.model.json)

    ```json
        ...

        ":items": {
            "text": {
                "text": "<p>Hello <b>L738</b>!</p>\n",
                "richText": true,
                ":type": "L738-angular/components/text"
            },
            "text_1800095243": {
                "text": "<h1>My First Heading</h1>\r\n",
                "richText": true,
                ":type": "L738-angular/components/text"
            }
        }
    ```

Observe the JSON data structure to have a better idea of how your component is represented. Note the internal fields prefixed with the colon character. These fields are mostly used by the SPA SDK to traverse the hierarchy of components and enable the dynamic instantiation of components. We will explore further how the model of the component is generated on the backend in [Lesson 6 - Back-end Development](#lesson-6---back-end-development)

#### Exercise 2.4 - Visualize the frontend text component source code

1. Return to **Visual Studio Code** IDE
2. Open the file(s) containing the source code of the **Text** componenent:

![react-logo](./images/react-logo.png) React:

 * **react-app** > **src** > **components** > **Text** > **Text.js**

![angular-logo](./images/angular-logo.png) Angular:

 * **angular-app** > **src** > **app** > **components** > **text** > **text.component.ts**

 * **angular-app** > **src** > **app** > **components** > **text** > **text.component.html**

3. Note how the _text_ field is made available and how it is *mapped* to the component.

## Lesson 3 - WKND Events App

### Objective

1. Install the **WKND Events App** to a local AEM instance.
2. Inspect a more mature SPA, built based on the SPA Starter kit.
3. Understand advanced use cases with the SPA Editor.

### Lesson Context

In this lesson we will install the WKND Events App. This application displays a series of upcoming events and information for the ficticious life-style brand WKND. The application provides an example how structured data can be combined with editorial content to create a rich and compelling experience. In the following lessons, the WKND Events App will be extended. 

> This version of the WKND Events app has been modified to include both React and Angular code bases. **Note** that this is **not** a standard, but done to make it easier to go through the lab material. In a real-world use case, typically only a single JS framework would be used.

#### Exercise 3.1 - Open WKND Events Project

1. Open **Visual Studio Code**
2. Click **Open Folder** and navigate to **Desktop** > **resources** > **lesson-3** > **aem-guides-wknd-events**
![wknd-events folder](./images/lesson-3/vscode-wknd-events.png)
3. There are 5 folders in the project that represent individual Maven modules
    * **angular-app** Angular version of WKND Events
    * **core**: OSGi bundle containing Java code
    * **react-app** React version of WKND Events
    * **ui.apps** - AEM components and dialog definitions
    * **ui.content** - AEM templates and configurations

### Exercise 3.2 - Build the WKND Events Project

1. In **Visual Studio Code** from menu bar > **Terminal** > **New Terminal**

    ![Open terminal](./images/open-terminal.png)

2. The terminal should default to be in the directory: `~/Desktop/resources/lesson-3/aem-guides-wknd-events`.
3. Run the following command in the terminal:

    ```
    $ mvn -PautoInstallPackage clean install
    ```
    This will build and deploy the application to a local instance of AEM running at [http://localhost:4502](http://localhost:4502).

    ```
    [INFO] ------------------------------------------------------------------------
    [INFO] Reactor Summary:
    [INFO]
    [INFO] aem-guides-wknd-events ............................. SUCCESS [  0.357 s]
    [INFO] WKND Events - Core ................................. SUCCESS [  2.692 s]
    [INFO] WKND Events - React App ............................ SUCCESS [ 59.979 s]
    [INFO] WKND Events - Angular App .......................... SUCCESS [ 32.656 s]
    [INFO] WKND Events - UI content ........................... SUCCESS [  5.754 s]
    [INFO] WKND Events - UI apps .............................. SUCCESS [  1.213 s]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    ```
4. Open the browser and navigate to AEM: [http://localhost:4502](http://localhost:4502).
5. Click **Tools** > **Deployment** > **Packages** to navigate to CRX Package Manager: [http://localhost:4502/crx/packmgr/index.jsp](http://localhost:4502/crx/packmgr/index.jsp)
6. You should see that the package has been deployed:

    ![WKND Events Packages](./images/lesson-3/wknd-packages.png)

### Exercise 3.3 - View the WKND Events App

1. From the AEM Start Menu [http://localhost:4502/aem/start.html](http://localhost:4502/aem/start.html) Navigate to **Sites**.

    ![Sites icon](./images/sites-icon.png)

2. ![React Logo](./images/react-logo.png) Navigate to **WKND Events** > **React** > **Home** and open the page using the Sites Editor:

    ![Open React Home](./images/lesson-3/sites-navigation.png)

    **or** ![Angular Logo](./images/angular-logo.png) Navigate to **WKND Events** > **Angular** > **Home** and open the page using the Sites Editor:
3. Inspect the application's home page. 
4. Switch the Sites Editor to **Preview** mode by clicking the **Preview** button in the upper right-hand corner: ![Preview](./images/lesson-3/preview-button.png).
5. Navigate to different parts of the app by clicking the links in the custom **List** component.

## Lesson 4 - JSON Contract

A new business requirement for the WKND Events App is to create a teaser-like component for promoted content. The mockup for the **Promo** component is below:

![Sponsored Content Mockups](./images/lesson-4/promo-mockup.png)

### Objective

1. Create a new component by starting with the JSON structure.
2. Learn how creating a JSON contract upfront can facilitate Front-end and back-end development in parallel.
3. Plan what parts of the component will be editable and the impact this has on the development responsibilities.

### Lesson Context

When planning a new component it is important to start with the JSON structure that will ultimately drive the SPA component. By defining the expected JSON structure upfront this allows both front-end and back-end developers to begin working in parallel. 

![Separation of Concerns](./images/lesson-4/separation-concerns.png)

#### Exercise 4.1 - Update the Mock JSON

1. Switch back to the **Visual Studio Code** IDE and open the **AEM Guides WKND Events** project (from Lesson 3).
2. Open the mock JSON file:
    * ![React Logo](./images/react-logo.png) React: **react-app** > **public** > Open the file **mock.model.json**.
    * ![Angular Logo](./images/angular-logo.png) Angular: **angurl-app** > **src** > **mocks** > **json** > **angular.model.json**
3. Find the JSON for the **Promo** component by searching for the keyword `promo`. The JSON structure for the component has already been partially stubbed-out. Note the internal field `:type` and the value of `wknd-events/components/content/promo`. This is the property that will tie the React component with the JSON properties.
4. Update the JSON to add a new property with a key of `offerText`:

    ```diff
    "promo": {
        "title": "WKNDs Free Pass",
        "description": "<p>Get a Monthly package handcrafted for you. Secret Shows, Special Discounts, limited access art shows. Discover your city events and venues in a smarter way, relevant to your interests.</p>\r\n",
    +    "offerText":"$200 / Month",
        "actionsEnabled": true,
        "actions": [
            {
                "title": "More",
                "url": "#"
            }
        ],
        "image": {
            "src": "/images/sponsored-light-big.jpg",
            "areas": [],
            "uuid": "26bce72d-b6f7-46a6-be70-c88221825e6a",
            "widths": [],
            "lazyEnabled": false,
            ":type": "wknd-events/components/content/image"
        },
        ":type": "wknd-events/components/content/promo"
    }
    ```

## Lesson 5 - Front-end Development

### Objective

1. Register a frontend component to map a backend data structure.
2. Learn how the data structure served by AEM is transformed into component properties.
3. Drive the content author experience from the frontend component.
4. Understand the role played by the ModelManager.
5. Get to know the main components provided by the SPA SDK.
6. Observe components becoming editable.

### Lesson Context

The lesson illustrates how editable frontend components are developed and how the AEM data is provided as properties. Get more familiar with the notion of library of frontend component and the dynamic instantiation of components. Better understand the motivations and advantages of delegating the content structure to AEM. Learn how to support the author's experience from within the frontend project.

#### ![React Logo](./images/react-logo.png) Exercise 5.1 - Start the development server

1. Within **Visual Studio Code** open the terminal (if not opened already: from menu bar > **Terminal** > **New Terminal**).
2. The terminal should already be in the directory: `~/Desktop/resources/lesson-3/aem-guides-wknd-events`.
3. Navigate to the `react-app` directory with the following command:

    ```
    $ cd react-app
    ```
4. Enter the following command to start the local developer server:

    ```
    $ npm start

    Compiled successfully!

    You can now view react-app in the browser.

    Local:            http://localhost:3000/
    On Your Network:  http://192.168.1.152:3000/

    Note that the development build is not optimized.
    To create a production build, use npm run build.
    ```
5. This should launch a new browser tab running at [http://localhost:3000](http://localhost:3000/). Navigate to [http://localhost:3000/content/wknd-events/react/home.html](http://localhost:3000/content/wknd-events/react/home.html)
6. You should now see the WKND Events App running, but using the Mock JSON file. **Note** the Promo component is not visible.

#### ![React Logo](./images/react-logo.png) Exercise 5.2 - Map JSON model to Promo Component

1. Within **Visual Studio Code** navigate to **react-app** > **src** > **components** > **promo**.
2. Open the file **Promo.js**. This is a partially stubbed out React component that will render the Promo content.
3. At the very bottom of the file, beneath the last `}` curly brace add the following line:

    ```js
    MapTo('wknd-events/components/content/promo')(Promo, PromoEditConfig);
    ```
    Save your changes.
4. Navigate to **react-app** > **src** > **components** and open the file **MappedComponents.js**.
5. Add the following line to the end of the file:

    ```js
    require('./promo/Promo');
    ```
    Save your changes.
6. Return to the browser and navigate to [http://localhost:3000/content/wknd-events/react/home.html](http://localhost:3000/content/wknd-events/react/home.html). You should now see a loosely styled version of the Promo component:

![unstyled promo](./images/lesson-5/unstyled-promo.png)

#### ![React Logo](./images/react-logo.png) Exercise 5.3 - Add Offer Text and Style

1. Return to **Visual Studio Code** and re-open **Promo.js** beneath **react-app** > **src** > **components** > **promo**.
2. In the `render()` method add the following snippet of JSX beneath the `h3` Promo Title:

    ```diff
    render() {
        ...
        <h3 className="Promo-title">{this.title}</h3>
    +   <h6 className="Promo-offer">{this.props.offerText}</h6>
        ...  
    }
    ```
    Save your changes. The component should now render the value of the property `offerText`. 
3. At the top of the file add the following line beneath the last `import` statement:

    ```diff
      import { Image } from '../image/Image';
    + import "./Promo.scss";
    ```
    Save your changes. This will now include some styles defined in the SaSS file `Promo.scss`.
4. Return to the browser and navigate to [http://localhost:3000/content/wknd-events/react/home.html](http://localhost:3000/content/wknd-events/react/home.html). You should now see a styled version of the Promo component with the `offerText` displayed.

    ![offer Text](./images/lesson-5/offer-text-styled.png)

## Lesson 6 - Back-end Development

### Objective

1. Familiarize yourself with the Sling Model Exporter.
2. Discover the interfaces that are at the heart of the communication layer between AEM and the frontend libraries.
3. Expose a new field by adding a simple getter.

### Lesson Context

The lesson explores how data is processed and serialized. Implement the interfaces that constitute the main building blocks of the data structure exposed to the frontend libraries. As the project progresses, expose new fields.

#### Exercise 6.1

## Lesson 7 - Navigation and Routing

### Objective

1. Understand relationship and differences between the App routing and the ModelRouter.
2. Consider the multiple degrees of editing capabilities: Portability and re-usability of the frontend script.

### Lesson Context

The lesson exposes the relationship and differences between the App routing and the ModelRouter. Optimize the data model initially delivered to the App and let the ModelRouter asynchronously load the remaining data. Learn how to build your frontend project to benefit from all the authoring capabilities offered by AEM.

#### Exercise 7.1

## Lesson 8 - Server Side Rendering

### Objective

1. Get more familiar with the Server Side Rendering of frontend components.
2. Improve the SEO of the project using Server Side Rendering.
3. Compare two possible SSR architectures and their trade-off.


### Lesson Context

The lesson provides examples of Server-Side Rendered SPA and exposes the main advantages of such an architecture. Compare different architectures and their trade-off.

![SSR](./images/server-side-rendering-cms-driven.png)

#### Exercise 8.1 Configure the Server-Side rendering

The _WKND - events_ project only contains a _React_ Server-Side environment. To proceed with the current exercise, please start with the provided initial content for chapter 8.

1. Modify the page component HTL template [a,b]
2. Start the local NODE.js server [c]
3. Request the page from your browser
4. Using a web browser, navigate to one of the pages of the app

a. Display the content of the HTL template for the body of the page component by navigating to the following URL
```
http://localhost:4502/crx/de/index.jsp#/apps/wknd-events/components/structure/page/body.html
```

b. Modify the content of the template as follow
```
<app-root id="root">
    <sly data-sly-resource="${resource @ resourceType='cq/remote/content/renderer'}" />
</app-root>
```

c. In your terminal, navigate inside the _react-app_ directory and execute the command to start the local _Node_ server
```
npm run start:server
```

d. Configure the AEM Remote HTML Renderer Servlet

5. Navigate to the System OSGi Configuration console (http://localhost:4502/system/console/configMgr)
6. Look for the configuration named `Remote Content Renderer - Configuration Factory`
7. Create a new configuration and set the after-mentioned field as follow

```
Content path pattern=/content/wknd-events/react(.*)
```

8. In your browser, display the Server-Side rendered home page of the SPA by navigating to http://localhost:4502/content/wknd-events/react/home.html 
9. Open the developer console, open the Network tab (alt+cmd+i)
10. Activate the filter called _XHR_
11. Refresh the page

![No model request](./images/wknd-events-ssr-network-no-model-request.png)

Note that there is no request to any model which would implay an entry for a URL ending with a the selector and extension such as _.model.json_

12. Open the page source of the page (alt+cmd+u or cmd+click > View Page Source)
13. In the page source, look for the tag with the id *__INITIAL_STATE__*

![SSR Model Initial State](./images/wknd-events-ssr-json-model-initial-state.png)

The page source exposes a source code that hasn't yet been manipulated by the SPA script. Note that the server-side script has dumped the JSON model object used to generate the initial content of the app. Finally, the frontend script present in the browser synchronizes itself with the provided JSON model object.

##### Exercise 8.1.1 Server-Side Rendering Fallback

Let's simulate a failure of the remote node server.

1. Kill the _Node_ server instance (ctrl+c)
2. Refresh the page source

![SSR failure - Page source](./images/wknd-events-ssr-page-source-error.png)

Note that in the `<app-root id="root">` tag is displayed a comment that contains the error code and message.

3. Refresh the page

![SSR failure - Network model request](./images/wknd-events-ssr-network-model-request.png)

Note that a request to the model of the app is sent to the AEM server. The client script is has not been synchronized and initialize itself independently from the server.

## Next Steps

At the end of your lab manual it is recommended to include a **Next Steps** section for lab attendees to continue learning.

## Additional Resources

## Appendix

An appendix section is also a nice to have and include reference links to documentation.
