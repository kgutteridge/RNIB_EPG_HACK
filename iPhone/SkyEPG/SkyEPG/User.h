//
//  User.h
//  SkyEPG
//
//  Created by Kieran Gutteridge on 11/02/2012.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "SynthesizeSingleton.h"

@interface User : NSObject


@property (nonatomic, retain) NSString *username, *password, *token, *key;

+(id)sharedUser;

-(void)readUserPrefs;
-(void)forgetUser;

-(void)doRegistrationWithUsername:(NSString *)username andPassword:(NSString *)password;


//http://www.sky.com/tvlistings-proxy/TVListingsProxy/remoteRecord.json?channelId=%(channel)d&eventId=%(event)d&siteId=1
-(void)recordShow:(NSString *)eventID onChannel:(NSString *)channel;

@end
