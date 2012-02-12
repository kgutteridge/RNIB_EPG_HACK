//
//  User.m
//  SkyEPG
//
//  Created by Kieran Gutteridge on 11/02/2012.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "User.h"
#import "AFNetworking.h"

@implementation User

static User *sharedUser = nil;

@synthesize username, password, token, key;


#pragma mark -
#pragma mark User Preferences
-(void)forgetUser
{
	NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
	
	[prefs removeObjectForKey:PREF_AUTOLOGIN];
	[prefs removeObjectForKey:PREF_TOKEN];
	[prefs synchronize];
	
	[self readUserPrefs];
}

-(void)readUserPrefs
{
	NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
	
	self.token = [prefs valueForKey:PREF_TOKEN];
	
}


-(void)doRegistrationWithUsername:(NSString *)uname andPassword:(NSString *)pass
{
    
    [[NSHTTPCookieStorage sharedHTTPCookieStorage] setCookieAcceptPolicy:NSHTTPCookieAcceptPolicyAlways];
    
    NSURL *url = [NSURL URLWithString:@"https://skyid.sky.com/"];
    AFHTTPClient *client = [[AFHTTPClient alloc] initWithBaseURL:url];
    

    [client postPath:@"signin" parameters:[NSDictionary dictionaryWithObjectsAndKeys:uname, @"username", pass, @"password", nil] 
             success:^(AFHTTPRequestOperation *operation, id responseObject) 
             {
                 NSString *received_txt = [[[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding] autorelease];   
                 NSLog(@"Stuff %@",received_txt);
                 NSLog(@"Request Headers %@",operation.response.allHeaderFields);                 
                 NSArray *cookieArray =[NSHTTPCookie cookiesWithResponseHeaderFields:operation.response.allHeaderFields forURL:[NSURL URLWithString:@"https://skyid.sky.com/signin"]];
                 
                 for (NSHTTPCookie* cookie in cookieArray)
                 {
                      NSLog(@"nName: %@nValue: %@nExpires: %@", [cookie name], [cookie value], [cookie expiresDate]);
                 }
                    
             } 
             failure:^(AFHTTPRequestOperation *operation, NSError *error) 
             {
                 NSLog(@"Error %@", error);
             }];
    [client release]; 

}


//http://www.sky.com/tvlistings-proxy/TVListingsProxy/remoteRecord.json?channelId=%(channel)d&eventId=%(event)d&siteId=1
-(void)recordShow:(NSString *)eventID onChannel:(NSString *)channel
{
    [[NSHTTPCookieStorage sharedHTTPCookieStorage] setCookieAcceptPolicy:NSHTTPCookieAcceptPolicyAlways];
    
    NSURL *url = [NSURL URLWithString:@"http://www.sky.com/"];
    AFHTTPClient *client = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:channel, @"channelId", eventID, @"eventId", @"1",@"siteId", nil];
    
    [client getPath:@"tvlistings-proxy/TVListingsProxy/remoteRecord.json" parameters: params
             success:^(AFHTTPRequestOperation *operation, id responseObject) 
     {
       
         NSString *received_txt = [[[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding] autorelease];   
         NSLog(@"Record show %@",received_txt);
    
     } 
             failure:^(AFHTTPRequestOperation *operation, NSError *error) 
     {
            NSLog(@"Error %@", error);
     }];
    [client release]; 
    
}




#pragma mark Singleton Methods
+(id)sharedUser
{
	@synchronized(self)
	{
		if(sharedUser == nil)
		{
			sharedUser = [[super allocWithZone:NULL] init];
			//[sharedUser readUserPrefs];
		}
	}
	return sharedUser;
}

+(id)allocWithZone:(NSZone *)zone
{
	return [[self sharedUser] retain];
}

-(id)copyWithZone:(NSZone *)zone
{
	return self;
}

-(id)retain
{
	return self;
}

-(unsigned)retainCount
{
	return UINT_MAX;
}

-(void)release
{
	//never release
}

-(id)autorelease
{
	return self;
}

-(id)init
{
	if(self = [super init])
	{
		//set properties to defaults
	}
	return self;
}

- (NSString *)description
{
	return [NSString stringWithFormat:@"EPG User"];
	
}



@end
