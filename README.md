# SMM Framework

The SMM Framework is an implementation of Eqasim + MATSim+ Sharing micromobility 
Creted by Juan Rendon at TU München
  The SMM Framework can be seen in the Examples folder> SMM Framework folded
 The main class for running is the RunSMMFramework
 
In order to run the class there must be a series of inputs that need to be inserted via program arguments 


An Input file of the framework is the General Bike Specification (GBFS). The General Bike Specification is an open data standard that informs the real-time status of shared mobility (NABSA). The GBFS defines multiple dimensions of shar-ing services, such as the stations, the vehicles, their status, and the price structure of the services, among others. However, the SMM framework only focuses on three pos-sible GBFS: station_information.json, station_status.json, and free_bike_status.json. When referring to station-based micromobility service, the framework must be provided with the station_information.json and sta-tion_status.json files. Conversely, the Free-floating scheme is implemented us-ing only the free_bike_status.json file. Finally, the description of each file and mandato-ry fields are as follows: 
·	Station_information.json: this file is the collection of parking stations of the service and their characteristics (NABSA). The SMM framework is crucial in providing identification for each station, the stations' coordinates, and each station's parking capacity. To provide that information, the JSON file provided must contain the fields “capacity,” "lat”, lon”, and “station_id”. 
·	Station_status.json: The station status file informs the number of vehicles available in the station (NABSA). In the context of the SMM framework, this file informs the sharing module of the number of vehicles per station at the begin-ning of the simulation day. Further, the JSON file must contain the fields “num_bikes_available” and “station_id”. 
·	Free_bike_status.json: The free vehicle file defines which vehicles are not being used at the current time the GBFS was generated and their location (NABSA, 2015/2022b).In the SMM framework context, this file informs of the location and the id of the service vehicles. Therefore, the JSON file must contain the values “bike_id,” lat,” and “lon”. 

The simulation parameters are as following:
·	Config Path: Provides the file path to the config file as a String. It is a mandatory input.
·	Station Information: If the desired service is an SB service, it provides the Sta-tion_information.json file path as a String.
·	Station Status: If the desired service is an SB service, it provides the Sta-tion_status.json path as a String.
·	Free Vehicles: If the desired service is a FF service, it provides the Free_bike_status.json path as a String.
·	Access / Egress Distance: The maximum access/egress distance the agents can walk to an SMM station or vehicle. It must be a Double value and is a mandatory parameter for the SMM framework.
·	Service Name: Describes the service name allocated to the service. It is a man-datory parameter for the SMM framework.
·	Mode: Provides the mode which will route the trips made using this mode. The possible values are the Strings “eScooter” and “Shared-Bike”. It’s a mandatory parame-ter for the SMM framework.
·	Service Area: If the desired service is a Free-floating service, it provides the path to the service area shapefile as a String. This parameter is optional for FF services.
·	Multimodal: Establish if the desired service can be used as an access/egress mode to public transport. The parameter must be entered as a String with possible val-ues "Yes" or “No”. It is a mandatory parameter for the SMM framework.
·	Service Scheme: Determines which service scheme the service will have, either a FF or SB scheme. The parameter must be entered as a String with possible values "Station-Based" or “Free Floating”. It’s a mandatory parameter for the SMM framework.
·	Sharing Trip Unlock Fee: Describes the cost of unlocking a SMM vehicle of the service. This parameter must be a Double and is mandatory for the SMM framework.
·	Sharing Trip per Min Cost: Describes the cost of using a SMM vehicle of the service per minute. This parameter must be a Double and is mandatory for the SMM framework.

·	Cost Parameters: Defines the cost in Euro/min for the different modes such as PT, car, bike, among others. The parameter must be a Double and is optional since the SMM includes pre-defined values .
·	Mode parameters: Defines the parameters for the utility function of the modes such as PT, car, bike, among others The parameter must be a Double and is optional since the SMM includes pre-defined values.

A summary of the simulation parameters can be seen as the following:
![parameters](https://github.com/rendon830/eqasim-java/blob/Clean/parameters.JPG)

An example of the simulation arguments as the following:
![parameters-Ex0](https://github.com/rendon830/eqasim-java/blob/Clean/exampleParam.JPG)


