
import React, {Component} from 'react';
import {MapTo} from '@adobe/cq-react-editable-components';
import { Button } from '../button/Button';
import { Image } from '../image/Image';
import "./Promo.scss";

/**
 * Promo Edit Config
 * 
 * @type EditConfig
 */
const PromoEditConfig = {

    emptyLabel: 'Promo',

    isEmpty: function(props) {
        return !props || !props.title;
    }
};

/**
 * 
 * Promo React component
 */
export default class Promo extends Component {

    get title() {
        if(this.props.title) {
            return this.props.title;
        }
        return null;
    }

    get description() {
        if(this.props.description) {
            return this.props.description;
        }
        return null;
    }

    get actionLinks() {

        if(this.props.actions) {
            return (<div className="Promo-actions">
                {
                    this.props.actions.map((actionItem, index) => {
                        return <Button key={index} url={actionItem.url}>{actionItem.title}</Button>
                       })
                }
                </div>);
        }

        return null;
    }

    get image() {
        if(this.props.image) {
            
            return <Image {...this.props.image} alt={this.title} />;
        }

        return null;
    }


    render() {
        if(!PromoEditConfig.isEmpty(this.props)) {
            return (<div className="Promo">
                <div className="Promo-image ">
                    {this.image}
                </div>
                <div className="Promo-content">
                    <span className="Promo-sponsor">Sponsored</span>
                    <h3 className="Promo-title">{this.title}</h3>
                    <h6 className="Promo-offer">{this.props.offerText}</h6>
                    <div className="Promo-description" dangerouslySetInnerHTML={{__html:  this.description}}/>
                    {this.actionLinks}
                </div>
            </div>);
        }
        return null;
    }
}

MapTo('wknd-events/components/content/promo')(Promo, PromoEditConfig);
