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

@end
